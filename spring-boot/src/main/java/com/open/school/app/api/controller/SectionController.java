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
import com.open.school.app.api.entity.SectionEntity;
import com.open.school.app.api.service.SectionServiceImpl;

@RestController
@RequestMapping("api/")
public class SectionController {

	@Autowired
	private SectionServiceImpl sectionServiceImp;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;

	@PreAuthorize("hasPermission('section', 'GET')")
	@RequestMapping(value = "section", method = RequestMethod.GET)
	public ResponseEntity<?> getSchools(@RequestParam long schoolId, @RequestParam long classId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return sectionServiceImp.getSectionBySchoolAndClass(schoolId, classId);
	}

	@PreAuthorize("hasPermission('section', 'POST')")
	@RequestMapping(value = "section", method = RequestMethod.POST)
	public ResponseEntity<?> saveSchool(@RequestBody SectionEntity section, @RequestParam long schoolId, @RequestParam long classId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return sectionServiceImp.saveSection(section, schoolId, classId);
	}

	@PreAuthorize("hasPermission('section', 'PUT')")
	@RequestMapping(value = "section", method = RequestMethod.PUT)
	public ResponseEntity<?> updateSchool(@RequestBody SectionEntity section, @RequestParam long schoolId, @RequestParam long classId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return sectionServiceImp.updateSection(section, schoolId, classId);
	}

	@PreAuthorize("hasPermission('section', 'DELETE')")
	@RequestMapping(value = "section", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteSchool(@RequestParam long schoolId, @RequestParam long sectionId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return sectionServiceImp.removeSection(schoolId, sectionId);
	}

}
