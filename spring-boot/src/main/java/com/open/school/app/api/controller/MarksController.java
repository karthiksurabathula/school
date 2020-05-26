package com.open.school.app.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.open.school.app.api.authorization.CustomPermissionEvaluator;
import com.open.school.app.api.model.marks.MarksReqModel;
import com.open.school.app.api.service.MarksServiceImpl;

@RestController
@RequestMapping("api/")
public class MarksController {

	@Autowired
	private CustomPermissionEvaluator customPrevEval;
	@Autowired
	private MarksServiceImpl marksServ;

	@PreAuthorize("hasPermission('marksBySubject', 'GET')")
	@RequestMapping(value = "marksBySubject", method = RequestMethod.GET)
	public ResponseEntity<?> getMarksBySubject(@RequestParam long schoolId, @RequestParam long examId, @RequestParam long subjectId, @RequestParam long classId, @RequestParam long sectionId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return marksServ.getMarkseBySchoolClassSectionAndSubject(schoolId, examId, subjectId, classId, sectionId);
	}

	@PreAuthorize("hasPermission('marksByStudent', 'GET')")
	@RequestMapping(value = "marksByStudent", method = RequestMethod.GET)
	public ResponseEntity<?> getMarksByStudentt(@RequestParam long schoolId, @RequestParam long examId, @RequestParam long studentId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return marksServ.getMarkseByStudentSchoolAndExam(schoolId, examId, studentId);
	}

	@PreAuthorize("hasPermission('marksBySubject', 'POST')")
	@RequestMapping(value = "marksBySubject", method = RequestMethod.POST)
	public ResponseEntity<?> saveMarksBySubject(@RequestBody List<MarksReqModel> marks, @RequestParam long schoolId, @RequestParam long examId, @RequestParam long subjectId, @RequestParam long classId, @RequestParam long sectionId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return marksServ.saveMarksBySubject(schoolId, examId, subjectId, classId, sectionId, marks);
	}
}
