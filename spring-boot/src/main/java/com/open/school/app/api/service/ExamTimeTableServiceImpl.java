package com.open.school.app.api.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.open.school.app.api.entity.ClassEntity;
import com.open.school.app.api.entity.ExamEntity;
import com.open.school.app.api.entity.ExamTimeTableEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.entity.SubjectClassMapEntity;
import com.open.school.app.api.entity.SubjectEntity;
import com.open.school.app.api.model.examTimetable.ExamTimeTableReq;
import com.open.school.app.api.repository.ExamRepository;
import com.open.school.app.api.repository.ExamTimeTableRepository;
import com.open.school.app.api.repository.SubjectClassMapRepository;

@Service
public class ExamTimeTableServiceImpl {

	@Autowired
	private ExamTimeTableRepository examTimeTableRepo;
	@Autowired
	private SubjectClassMapRepository subjectClassMapRepository;
	@Autowired
	private ExamRepository examRepo;
	SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
	
	private static final Logger log = LogManager.getLogger(ExamServiceImpl.class);
	


	public ResponseEntity<?> getTimeTableBySchoolClassAndExam(long schoolId, long classId, long examId) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> schedule = new ArrayList<Object>();
		String message = null;
		try {
			List<ExamTimeTableEntity> examTimeTable = examTimeTableRepo.getExamTimetableByClass(schoolId, classId, examId);
			if (examTimeTable.size() > 0) {
				for (int i = 0; i < examTimeTable.size(); i++) {
					HashMap<String, Object> timetable = new HashMap<>();
					timetable.put("subject", examTimeTable.get(i).getSubject());
					timetable.put("timetable", examTimeTable.get(i));
					schedule.add(timetable);
				}
				response.put("exam", examTimeTable.get(0).getExam());
				response.put("schedule", schedule);
				response.put("indicator", "success");
			} else {
				List<SubjectClassMapEntity> subjectClassMap = subjectClassMapRepository.getSubjectClassMapByClass(schoolId, classId);
				if (subjectClassMap == null) {
					response.put("message", "No Subjects Found");
				} else {
					for (int i = 0; i < subjectClassMap.size(); i++) {
						HashMap<String, Object> timetable = new HashMap<>();
						timetable.put("subject", subjectClassMap.get(i).getSubject());
						timetable.put("timetable", new ExamTimeTableEntity(0, 0, null, null, null, null, null, null, null));
						schedule.add(timetable);
					}
					response.put("exam", examRepo.findById(examId).get());
					response.put("schedule", schedule);
					response.put("indicator", "success");
				}
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

	public ResponseEntity<?> getTimeTableBySchoolAndExam(long schoolId, long examId) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> schedule = new ArrayList<Object>();
		String message = null;
		try {
			List<ExamTimeTableEntity> examTimeTable = examTimeTableRepo.getExamTimetableOfSchoolByExam(schoolId, examId);
			if (examTimeTable.size() > 0) {
				for (int i = 0; i < examTimeTable.size(); i++) {
					HashMap<String, Object> timetable = new HashMap<>();
					timetable.put("subject", examTimeTable.get(i).getSubject());
					timetable.put("timetable", examTimeTable.get(i));
					schedule.add(timetable);
				}
				response.put("exam", examTimeTable.get(0).getExam());
				response.put("schedule", schedule);
				response.put("indicator", "success");
			} else {
				List<SubjectClassMapEntity> subjectClassMap = subjectClassMapRepository.getSubjectClassMapBySchool(schoolId);
				if (subjectClassMap == null) {
					response.put("message", "No Subjects Found");
				} else {
					for (int i = 0; i < subjectClassMap.size(); i++) {
						HashMap<String, Object> timetable = new HashMap<>();
						timetable.put("subject", subjectClassMap.get(i).getSubject());
						timetable.put("timetable", new ExamTimeTableEntity(0, 0, null, null, null, null, null, null, null));
						schedule.add(timetable);
					}
					response.put("exam", examRepo.findById(examId).get());
					response.put("schedule", schedule);
					response.put("indicator", "success");
				}
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

	public ResponseEntity<?> saveExamTimeTable(long schoolId, long classId, long examId, ExamTimeTableReq timetable) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			for (int i = 0; i < timetable.getSchedule().size(); i++) {
				ExamTimeTableEntity actTimetable = examTimeTableRepo.getExamTimetableById(timetable.getSchedule().get(i).getTimetable().getId(), schoolId, classId, examId);
				if (actTimetable == null) {
					ExamTimeTableEntity examTimetable = new ExamTimeTableEntity();
					examTimetable.setMarks(timetable.getSchedule().get(i).getTimetable().getMarks());
					if (timetable.getSchedule().get(i).getTimetable().getDate() == null) {
						examTimetable.setDate(null);
					} else {
						examTimetable.setDate(formatter.parse(timetable.getSchedule().get(i).getTimetable().getDate()));
					}
					examTimetable.setModifiedBy(auth.getName());
					examTimetable.setExam(new ExamEntity(examId, null, null, null, null, null, null, false, null, null, null, null));
					examTimetable.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
					examTimetable.setClasses(new ClassEntity(classId, null, null, null, null, true, null, null));
					examTimetable.setSubject(new SubjectEntity(timetable.getSchedule().get(i).getSubject().getId(), null, null, null, null, null, null));
					examTimeTableRepo.saveAndFlush(examTimetable);
				} else {
					actTimetable.setMarks(timetable.getSchedule().get(i).getTimetable().getMarks());
					if (timetable.getSchedule().get(i).getTimetable().getDate() == null) {
						actTimetable.setDate(null);
					} else {
						actTimetable.setDate(formatter.parse(timetable.getSchedule().get(i).getTimetable().getDate()));
					}
					actTimetable.setExam(new ExamEntity(examId, null, null, null, null, null, null, false, null, null, null, null));
					actTimetable.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
					actTimetable.setClasses(new ClassEntity(classId, null, null, null, null, true, null, null));
					actTimetable.setSubject(new SubjectEntity(timetable.getSchedule().get(i).getSubject().getId(), null, null, null, null, null, null));
					examTimeTableRepo.saveAndFlush(actTimetable);
				}

			}
			response.put("indicator", "success");
			response.put("message", "Exam timetable saved successfully");
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
