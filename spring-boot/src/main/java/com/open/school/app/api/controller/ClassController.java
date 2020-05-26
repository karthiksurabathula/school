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
import com.open.school.app.api.entity.ClassEntity;
import com.open.school.app.api.service.ClassServiceImpl;

@RestController
@RequestMapping("api/")
public class ClassController {

	@Autowired
	private ClassServiceImpl classServiceImpl;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;

	@PreAuthorize("hasPermission('class', 'GET')")
	@RequestMapping(value = "class", method = RequestMethod.GET)
	public ResponseEntity<?> getClasses(@RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return classServiceImpl.getClasses(schoolId);
	}

	@PreAuthorize("hasPermission('class', 'POST')")
	@RequestMapping(value = "class", method = RequestMethod.POST)
	public ResponseEntity<?> saveClass(@RequestBody ClassEntity classs, @RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return classServiceImpl.saveClass(classs, schoolId);
	}

	@PreAuthorize("hasPermission('class', 'PUT')")
	@RequestMapping(value = "class", method = RequestMethod.PUT)
	public ResponseEntity<?> updateClass(@RequestBody ClassEntity classs, @RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return classServiceImpl.updateClass(classs, schoolId);
	}

	@PreAuthorize("hasPermission('class', 'DELETE')")
	@RequestMapping(value = "class", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteClass(@RequestParam long classId, @RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return classServiceImpl.removeClass(schoolId, classId);
	}

}