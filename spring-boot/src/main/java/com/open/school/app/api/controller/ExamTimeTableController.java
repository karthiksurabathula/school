package com.open.school.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.open.school.app.api.authorization.CustomPermissionEvaluator;
import com.open.school.app.api.model.examTimetable.ExamTimeTableReq;
import com.open.school.app.api.service.ExamTimeTableServiceImpl;

@RestController
@RequestMapping("api/")
public class ExamTimeTableController {

	@Autowired
	private ExamTimeTableServiceImpl examTimetable;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;

	@PreAuthorize("hasPermission('examTimetable', 'GET')")
	@RequestMapping(value = "examTimetable", method = RequestMethod.GET)
	public ResponseEntity<?> getTimetableOfExamByClass(@RequestParam long schoolId, @RequestParam long classId, @RequestParam long examId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return examTimetable.getTimeTableBySchoolClassAndExam(schoolId, classId, examId);
	}

	@PreAuthorize("hasPermission('examTimetableSchool', 'GET')")
	@RequestMapping(value = "examTimetableSchool", method = RequestMethod.GET)
	public ResponseEntity<?> getTimetableOfExamBySchool(@RequestParam long schoolId, @RequestParam long examId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return examTimetable.getTimeTableBySchoolAndExam(schoolId, examId);
	}

	@PreAuthorize("hasPermission('examTimetable', 'POST')")
	@RequestMapping(value = "examTimetable", method = RequestMethod.POST)
	public ResponseEntity<?> saveTimetableOfExamByClass(@RequestBody ExamTimeTableReq timetable, @RequestParam long schoolId, @RequestParam long classId, @RequestParam long examId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return examTimetable.saveExamTimeTable(schoolId, classId, examId, timetable);
	}

}
