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
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.service.SchoolServiceImpl;

@RestController
@RequestMapping("api/")
public class SchoolController {

	@Autowired
	private SchoolServiceImpl schoolServiceImp;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;

	@PreAuthorize("hasPermission('school', 'GET')")
	@RequestMapping(value = "school", method = RequestMethod.GET)
	public ResponseEntity<?> getSchools(@RequestParam long cityId) {
		return schoolServiceImp.getSchools(cityId);
	}

	@PreAuthorize("hasPermission('schoolByUser', 'GET')")
	@RequestMapping(value = "schoolByUser", method = RequestMethod.GET)
	public ResponseEntity<?> getSchools() {
		return schoolServiceImp.getSchoolsByUser();
	}
	
	@RequestMapping(value = "schoolInfo", method = RequestMethod.GET)
	public ResponseEntity<?> getSchoolsById(@RequestParam long schoolId) {
		return schoolServiceImp.getSchoolsById(schoolId);
	}

	@PreAuthorize("hasPermission('school', 'POST')")
	@RequestMapping(value = "school", method = RequestMethod.POST)
	public ResponseEntity<?> saveSchool(@RequestBody SchoolEntity school, @RequestParam long cityId) {
		return schoolServiceImp.saveSchool(school, cityId);
	}

	@PreAuthorize("hasPermission('school', 'PUT')")
	@RequestMapping(value = "school", method = RequestMethod.PUT)
	public ResponseEntity<?> updateSchool(@RequestBody SchoolEntity school) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(school.getId());
		if (resp != null)
			return resp;
		else
			return schoolServiceImp.updateSchool(school);
	}

	@PreAuthorize("hasPermission('school', 'DELETE')")
	@RequestMapping(value = "school", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteSchool(@RequestParam long schoolId) {
		return schoolServiceImp.removeSchool(schoolId);
	}

}
