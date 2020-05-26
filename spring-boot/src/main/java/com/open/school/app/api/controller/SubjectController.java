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
import com.open.school.app.api.entity.SubjectEntity;
import com.open.school.app.api.model.CustomRequest;
import com.open.school.app.api.service.SubjectClassMapServiceImpl;
import com.open.school.app.api.service.SubjectServiceImpl;

@RestController
@RequestMapping("api/")
public class SubjectController {

	@Autowired
	private SubjectServiceImpl subjectServiceImpl;
	@Autowired
	private SubjectClassMapServiceImpl subjectClassMapServiceImpl;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;

	@PreAuthorize("hasPermission('subject', 'GET')")
	@RequestMapping(value = "subject", method = RequestMethod.GET)
	public ResponseEntity<?> getSubjects(@RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return subjectServiceImpl.getSubjects(schoolId);
	}

	@PreAuthorize("hasPermission('subject', 'POST')")
	@RequestMapping(value = "subject", method = RequestMethod.POST)
	public ResponseEntity<?> saveSubject(@RequestParam long schoolId, @RequestBody SubjectEntity subject) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return subjectServiceImpl.saveSubject(subject, schoolId);
	}

	@PreAuthorize("hasPermission('subject', 'PUT')")
	@RequestMapping(value = "subject", method = RequestMethod.PUT)
	public ResponseEntity<?> updateSubject(@RequestParam long schoolId, @RequestBody SubjectEntity subject) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return subjectServiceImpl.updateSubject(subject, schoolId);
	}

	@PreAuthorize("hasPermission('subject', 'DELETE')")
	@RequestMapping(value = "subject", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteSubject(@RequestParam long schoolId, @RequestParam long subjectId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return subjectServiceImpl.removeSubject(schoolId, subjectId);
	}

	@PreAuthorize("hasPermission('subjectClassMap', 'POST')")
	@RequestMapping(value = "subjectClassMap", method = RequestMethod.POST)
	public ResponseEntity<?> saveSubjectMap(@RequestBody CustomRequest subjectClassMap) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(subjectClassMap.getSchoolId());
		if (resp != null)
			return resp;
		else
			return subjectClassMapServiceImpl.createClassSubjectMap(subjectClassMap);
	}

	@PreAuthorize("hasPermission('subjectClassMap', 'PUT')")
	@RequestMapping(value = "subjectClassMap", method = RequestMethod.PUT)
	public ResponseEntity<?> updateSubjectMap(@RequestBody CustomRequest subjectClassMap) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(subjectClassMap.getSchoolId());
		if (resp != null)
			return resp;
		else
			return subjectClassMapServiceImpl.updateClassSubjectMap(subjectClassMap);
	}

	@PreAuthorize("hasPermission('subjectClassMap', 'DELETE')")
	@RequestMapping(value = "subjectClassMap", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteSubjectMap(@RequestParam long subjectMapId, @RequestParam long schoolId) {
		return subjectClassMapServiceImpl.removeSubjectClassMap(subjectMapId, schoolId);
	}

	@PreAuthorize("hasPermission('subjectClassMap', 'GET')")
	@RequestMapping(value = "subjectClassMap", method = RequestMethod.GET)
	public ResponseEntity<?> getSubjectMap(@RequestParam long schoolId, @RequestParam long classId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return subjectClassMapServiceImpl.getSubjectsBySchoolAndClass(schoolId, classId);
	}
}
