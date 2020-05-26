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
import com.open.school.app.api.entity.StudentEntity;
import com.open.school.app.api.model.CustomRequest;
import com.open.school.app.api.service.StudentMapServiceImpl;
import com.open.school.app.api.service.StudentServiceImpl;

@RestController
@RequestMapping("api/")
public class StudentController {

	@Autowired
	private StudentServiceImpl studentServiceImp;
	@Autowired
	private StudentMapServiceImpl studentMapServiceImpl;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;

	@PreAuthorize("hasPermission('studentMap', 'POST')")
	@RequestMapping(value = "student/map", method = RequestMethod.POST)
	public ResponseEntity<?> saveStudentMap(@RequestBody CustomRequest request) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(request.getSchoolId());
		if (resp != null)
			return resp;
		else
			return studentMapServiceImpl.createMap(request);
	}

	@PreAuthorize("hasPermission('studentMap', 'GET')")
	@RequestMapping(value = "student/map", method = RequestMethod.GET)
	public ResponseEntity<?> getStudentMapping(@RequestParam long schoolId, @RequestParam long studentId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return studentMapServiceImpl.getSudentMap(schoolId, studentId);
	}

	@PreAuthorize("hasPermission('student', 'GET')")
	@RequestMapping(value = "student", method = RequestMethod.GET)
	public ResponseEntity<?> getStudentById(@RequestParam long schoolId, @RequestParam String studentId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return studentServiceImp.getStudentsByStudentId(schoolId, studentId);
	}

	@PreAuthorize("hasPermission('studentByClass', 'GET')")
	@RequestMapping(value = "studentByClass", method = RequestMethod.GET)
	public ResponseEntity<?> getStudentByClassId(@RequestParam long schoolId, @RequestParam long classId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return studentServiceImp.getStudentsbyClass(schoolId, classId);
	}

	@PreAuthorize("hasPermission('student', 'POST')")
	@RequestMapping(value = "student", method = RequestMethod.POST)
	public ResponseEntity<?> saveStudent(@RequestBody StudentEntity student, @RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return studentServiceImp.saveStudent(student, schoolId);
	}

	@PreAuthorize("hasPermission('student', 'PUT')")
	@RequestMapping(value = "student", method = RequestMethod.PUT)
	public ResponseEntity<?> updateStudent(@RequestBody StudentEntity student, @RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return studentServiceImp.updateStudent(student, schoolId);
	}

	@PreAuthorize("hasPermission('student', 'DELETE')")
	@RequestMapping(value = "student", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteStudent(@RequestParam long schoolId, @RequestParam long studentId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return studentServiceImp.removeStudent(schoolId, studentId);
	}

	@PreAuthorize("hasPermission('studentPending', 'GET')")
	@RequestMapping(value = "student/pending", method = RequestMethod.GET)
	public ResponseEntity<?> getPendingStudentBySchoolId(@RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return studentServiceImp.getPendingStudentsBySchoolId(schoolId);
	}
	
	@PreAuthorize("hasPermission('studentById', 'GET')")
	@RequestMapping(value = "studentById", method = RequestMethod.GET)
	public ResponseEntity<?> getStudentById(@RequestParam long schoolId,@RequestParam long studentId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return studentServiceImp.getStudentsById(schoolId, studentId);
	}
}
