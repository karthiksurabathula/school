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
import com.open.school.app.api.entity.ExamNotesEntity;
import com.open.school.app.api.service.ExamNoteServiceImpl;

@RestController
@RequestMapping("api/")
public class ExamNotesController {

	@Autowired
	private ExamNoteServiceImpl examNotesServiceImpl;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;

	@PreAuthorize("hasPermission('examNotes', 'POST')")
	@RequestMapping(value = "examNotes", method = RequestMethod.POST)
	public ResponseEntity<?> saveExam(@RequestBody ExamNotesEntity examNotes, @RequestParam long schoolId, @RequestParam long examId, @RequestParam long classId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return examNotesServiceImpl.saveExamNotes(examNotes, schoolId, examId, classId);
	}

	@PreAuthorize("hasPermission('examNotes', 'GET')")
	@RequestMapping(value = "examNotes", method = RequestMethod.GET)
	public ResponseEntity<?> getExam(@RequestParam long schoolId, @RequestParam long examId, @RequestParam long classId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return examNotesServiceImpl.getNotesByExamAndSchool(schoolId, examId, classId);
	}

}
