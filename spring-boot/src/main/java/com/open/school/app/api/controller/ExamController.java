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
import com.open.school.app.api.entity.ExamEntity;
import com.open.school.app.api.service.ExamServiceImpl;

@RestController
@RequestMapping("api/")
public class ExamController {

	@Autowired
	private ExamServiceImpl examServiceImpl;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;

	@PreAuthorize("hasPermission('exam', 'POST')")
	@RequestMapping(value = "exam", method = RequestMethod.POST)
	public ResponseEntity<?> saveExam(@RequestBody ExamEntity exam, @RequestParam long schoolId, @RequestParam long classId, @RequestParam long sectionId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return examServiceImpl.createExam(exam, schoolId, classId, sectionId);
	}

	@PreAuthorize("hasPermission('exam', 'PUT')")
	@RequestMapping(value = "exam", method = RequestMethod.PUT)
	public ResponseEntity<?> updateExam(@RequestBody ExamEntity exam, @RequestParam long schoolId, @RequestParam long classId, @RequestParam long sectionId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return examServiceImpl.updateExam(exam, schoolId, classId, sectionId);
	}

	@PreAuthorize("hasPermission('exam', 'GET')")
	@RequestMapping(value = "exam", method = RequestMethod.GET)
	public ResponseEntity<?> getExam(@RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return examServiceImpl.getExamsBySchool(schoolId);
	}
	
	@PreAuthorize("hasPermission('examByClass', 'GET')")
	@RequestMapping(value = "examByClass", method = RequestMethod.GET)
	public ResponseEntity<?> getExam(@RequestParam long schoolId,@RequestParam long classId,@RequestParam long sectionId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return examServiceImpl.getExamsBySchoolAndClassAndSection(schoolId,classId,sectionId);
	}

	@PreAuthorize("hasPermission('examBySchool', 'GET')")
	@RequestMapping(value = "examBySchool", method = RequestMethod.GET)
	public ResponseEntity<?> getExamByScopeSchool(@RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return examServiceImpl.getExamByScopeSchool(schoolId);
	}

	@PreAuthorize("hasPermission('exam', 'DELETE')")
	@RequestMapping(value = "exam", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteExam(@RequestParam long schoolId, @RequestParam long examId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return examServiceImpl.deleteExams(examId, schoolId);
	}

}
