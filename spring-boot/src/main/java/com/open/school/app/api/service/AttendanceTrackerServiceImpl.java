package com.open.school.app.api.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.AttendenceTrackerEntity;
import com.open.school.app.api.entity.ClassEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.entity.SectionEntity;
import com.open.school.app.api.entity.WorkingDayEntity;
import com.open.school.app.api.model.attendance.AttendanceReqModel;
import com.open.school.app.api.repository.AttendenceTrackerRepository;
import com.open.school.app.api.repository.ClassRepository;
import com.open.school.app.api.repository.SectionRepository;
import com.open.school.app.api.repository.WorkingDayRepository;

@Service
public class AttendanceTrackerServiceImpl {

	@Autowired
	private AttendenceTrackerRepository attendenceTrackerRepo;
	@Autowired
	private ClassRepository classrepo;
	@Autowired
	private SectionRepository sectionRepo;
	@Autowired
	private WorkingDayRepository dayRepo;

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private static final Logger log = LogManager.getLogger(AttendanceTrackerServiceImpl.class);

	public void createEntriesForAttendenceTracker(long dayId, long schoolId) {

		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			List<ClassEntity> classes = classrepo.getClassessBySchool(schoolId);
			for (int i = 0; i < classes.size(); i++) {
				List<SectionEntity> sections = sectionRepo.getBySectionsBySchoolAndClass(schoolId, classes.get(i).getId());
				for (int j = 0; j < sections.size(); j++) {
					AttendenceTrackerEntity attendanceTracker = new AttendenceTrackerEntity();
					attendanceTracker.setLastModified(new Date());
					attendanceTracker.setModifiedBy(auth.getName());
					attendanceTracker.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
					attendanceTracker.setClasses(new ClassEntity(classes.get(i).getId(), null, null, null, null, true, null, null));
					attendanceTracker.setSection(new SectionEntity(sections.get(j).getId(), null, true, null, null, null, null, null, null));
					attendanceTracker.setDay(new WorkingDayEntity(dayId, null, null, null, null, null));
					attendanceTracker.setCompleted(false);
					attendenceTrackerRepo.saveAndFlush(attendanceTracker);
				}
			}
		} catch (Exception e) {
			log.error("", e);

		}

	}

	public ResponseEntity<?> getAttendanceStatus(long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> attendanceTrackerResp = new ArrayList<Object>();
		String message = null;
		try {
			WorkingDayEntity day = dayRepo.getWorkingDayByDate(schoolId, dateFormat.parse(dateFormat.format(new Date())));
			if (day != null) {
				List<AttendenceTrackerEntity> tracker = attendenceTrackerRepo.getAttendanceTrackerBySchoolAndDay(schoolId, day.getId());
				for (int i = 0; i < tracker.size(); i++) {
					AttendanceReqModel attendanceReqTemp = new AttendanceReqModel();
					attendanceReqTemp.setClasses(tracker.get(i).getClasses());
					attendanceReqTemp.setSection(tracker.get(i).getSection());
					attendanceReqTemp.setAttendanceTracker(tracker.get(i));
					attendanceTrackerResp.add(attendanceReqTemp);
				}
				response.put("data", attendanceTrackerResp);
				response.put("indicator", "success");
			} else {
				response.put("indicator", "fail");
				response.put("message", "Please initialize working day!!");
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

}
