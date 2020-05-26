package com.open.school.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.open.school.app.api.authorization.CustomPermissionEvaluator;
import com.open.school.app.api.service.SubjectStaffMapServiceImpl;

@RestController
@RequestMapping("api/")
public class SubjectStaffMapController {

	@Autowired
	private SubjectStaffMapServiceImpl subjectStaffMapService;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;

	@PreAuthorize("hasPermission('subjectStaffMap', 'POST')")
	@RequestMapping(value = "subjectStaffMap", method = RequestMethod.POST)
	public ResponseEntity<?> saveSchool(@RequestParam long schoolId, @RequestParam long classId, @RequestParam long sectionId, @RequestParam long subjectId, @RequestParam long staffId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return subjectStaffMapService.addSubjectStaffMap(schoolId, classId, subjectId, staffId, sectionId);
	}

	@PreAuthorize("hasPermission('subjectStaffMap', 'GET')")
	@RequestMapping(value = "subjectStaffMap", method = RequestMethod.GET)
	public ResponseEntity<?> saveSchool(@RequestParam long schoolId, @RequestParam long classId, @RequestParam long subjectId, @RequestParam long sectionId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return subjectStaffMapService.getSubjectStaffMap(schoolId, classId, subjectId, sectionId);
	}
}
