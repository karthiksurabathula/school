package com.open.school.app.api.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.CalendarDaysEntity;
import com.open.school.app.api.entity.ClassEntity;
import com.open.school.app.api.entity.PeriodEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.entity.SectionEntity;
import com.open.school.app.api.entity.StaffEntity;
import com.open.school.app.api.entity.SubjectEntity;
import com.open.school.app.api.entity.TimeTableEntity;
import com.open.school.app.api.model.period.PeriodReqModel;
import com.open.school.app.api.model.timetable.TimeTableReq;
import com.open.school.app.api.repository.PeriodRepository;
import com.open.school.app.api.repository.SubjectStaffMapRepository;
import com.open.school.app.api.repository.TimeTableRepository;

@Service
public class TimeTableServiceImpl {

	@Autowired
	private TimeTableRepository timeTableRepository;
	@Autowired
	private PeriodRepository periodRepository;
	@Autowired
	private SubjectStaffMapRepository staffMapRepo;

	DateFormat format = new SimpleDateFormat("hh:mma", Locale.ENGLISH);
	
	private static final Logger log = LogManager.getLogger(TimeTableServiceImpl.class);


	public ResponseEntity<?> saveTimeTable(long schoolId, long classId, long sectionId, String day, TimeTableReq timeTableRequest) {

		HashMap<String, Object> response = new HashMap<>();
		String message = "";

		try {
			for (int i = 0; i < timeTableRequest.getTimeTable().size(); i++) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if (timeTableRequest.getTimeTable().get(i).getSubject().getId() != 0) {
					TimeTableEntity timetable = timeTableRepository.getTimeTableBySectionAndDay(schoolId, classId, sectionId, day, timeTableRequest.getTimeTable().get(i).getPeriod().getId());
					if (timetable == null) {
						TimeTableEntity timeTableNew = new TimeTableEntity();
						timeTableNew.setModifiedBy(auth.getName());
						timeTableNew.setCreatedDate(new Date());
						timeTableNew.setLastModified(new Date());
						timeTableNew.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
						timeTableNew.setClasses(new ClassEntity(classId, null, null, null, null, true, null, null));
						timeTableNew.setSection(new SectionEntity(sectionId, null, true, null, null, null, null, null, null));
						timeTableNew.setSubject(new SubjectEntity(timeTableRequest.getTimeTable().get(i).getSubject().getId(), null, null, null, null, null, null));
						timeTableNew.setCalendarDay(new CalendarDaysEntity(day, null));
						timeTableNew.setPeriod(new PeriodEntity(timeTableRequest.getTimeTable().get(i).getPeriod().getId(), null, 0, 0, null, null, null, null));
						timeTableRepository.saveAndFlush(timeTableNew);
					} else {
						timetable.setModifiedBy(auth.getName());
						timetable.setLastModified(new Date());
						timetable.setSubject(new SubjectEntity(timeTableRequest.getTimeTable().get(i).getSubject().getId(), null, null, null, null, null, null));
						timeTableRepository.saveAndFlush(timetable);
					}
				}
			}
			response.put("message", "Time Table Created successfully");
			response.put("indicator", "success");
			response.put("timetable", timeTableRequest);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1002");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> getTimeTableBySchoolClassSectionAndDay(long schoolId, long classId, long sectionId, String day) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> peiodSubjectResp = new ArrayList<Object>();

		String message = null;
		try {
			List<PeriodEntity> periods = periodRepository.getPeriodsBySchool(schoolId);
			if (periods.size() > 0) {
				response.put("indicator", "success");
				for (int i = 0; i < periods.size(); i++) {
					HashMap<String, Object> peiodSubject = new HashMap<>();
					TimeTableEntity timetable = timeTableRepository.getTimeTableBySectionAndDay(schoolId, classId, sectionId, day, periods.get(i).getId());
					PeriodReqModel periodResp = new PeriodReqModel();
					periodResp.setId(periods.get(i).getId());
					periodResp.setDescription(periods.get(i).getDescription());
					periodResp.setStartTime(format.format(periods.get(i).getStartTime()));
					periodResp.setEndTime(format.format(periods.get(i).getEndTime()));
					peiodSubject.put("period", periodResp);
					if (timetable == null) {
						peiodSubject.put("subject", new SubjectEntity(0, null, null, null, null, null, null));
					} else {
						peiodSubject.put("subject", timetable.getSubject());
						Object result = staffMapRepo.getStaffNameBySubjectClassSectionSchool(schoolId, timetable.getSubject().getId(), classId, sectionId);
						StaffEntity staffResp = new StaffEntity();
						staffResp.setName((String)result);
						peiodSubject.put("staff", staffResp);
					}
					
					peiodSubjectResp.add(peiodSubject);
				}
				response.put("timeTable", peiodSubjectResp);
			} else {
				response.put("indicator", "fail");
				response.put("message", "No Periods Found");
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1001");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> getTimeTableByStaff(long schoolId, String day) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> peiodSubjectResp = new ArrayList<Object>();

		String message = null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			List<Object[]> periods = staffMapRepo.getStaffTimeTable(schoolId, auth.getName(), day);

			if (periods.size() > 0) {
				response.put("indicator", "success");
				for (Object[] result : periods) {
					HashMap<String, Object> peiodSubject = new HashMap<>();

					PeriodReqModel periodResp = new PeriodReqModel();
					periodResp.setStartTime(format.format((long) result[0]));
					periodResp.setEndTime(format.format((long) result[1]));

					ClassEntity classObj = new ClassEntity();
					classObj.setClassName((String) result[2]);

					SectionEntity section = new SectionEntity();
					section.setSectionName((String) result[3]);

					SubjectEntity subject = new SubjectEntity();
					subject.setSubjectName((String) result[4]);

					peiodSubject.put("period", periodResp);
					peiodSubject.put("class", classObj);
					peiodSubject.put("section", section);
					peiodSubject.put("subject", subject);
					peiodSubjectResp.add(peiodSubject);
				}
				response.put("timeTable", peiodSubjectResp);
			} else {
				response.put("indicator", "fail");
				response.put("message", "No Periods Found");
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (

		Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1001");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
