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
import com.open.school.app.api.entity.SyllabusEntity;
import com.open.school.app.api.service.SyllabusService;

@RestController
@RequestMapping("api/")
public class SyllabusController {

	@Autowired
	private CustomPermissionEvaluator customPrevEval;
	@Autowired
	private SyllabusService syllabusService;

	@RequestMapping(value = "syllabus", method = RequestMethod.GET)
	public ResponseEntity<?> getSyllabusUpdates(@RequestParam long schoolId, @RequestParam long classId, @RequestParam long sectionId, @RequestParam long subjectId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return syllabusService.getSyllabus(schoolId, classId, sectionId, subjectId);
	}

	@PreAuthorize("hasPermission('syllabus', 'POST')")
	@RequestMapping(value = "syllabus", method = RequestMethod.POST)
	public ResponseEntity<?> postAnnouncements(@RequestBody SyllabusEntity syllabus, @RequestParam long schoolId, @RequestParam long classId, @RequestParam long sectionId, @RequestParam long subjectId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return syllabusService.syllabusUpdate(schoolId, classId, sectionId, subjectId, syllabus);
	}

}
