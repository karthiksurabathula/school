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
import com.open.school.app.api.entity.StaffEntity;
import com.open.school.app.api.service.StaffServiceImpl;

@RestController
@RequestMapping("api/")
public class StaffController {

	@Autowired
	private StaffServiceImpl staffService;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;

	@PreAuthorize("hasPermission('staff', 'POST')")
	@RequestMapping(value = "staff", method = RequestMethod.POST)
	public ResponseEntity<?> saveStaff(@RequestBody StaffEntity staff, @RequestParam long schoolId, @RequestParam String role) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return staffService.saveStaff(staff, schoolId, role);
	}

	@PreAuthorize("hasPermission('staff', 'PUT')")
	@RequestMapping(value = "staff", method = RequestMethod.PUT)
	public ResponseEntity<?> updateStaff(@RequestBody StaffEntity staff, @RequestParam long schoolId, @RequestParam String role) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return staffService.updateStaff(staff, schoolId, role);
	}

	@PreAuthorize("hasPermission('staff', 'GET')")
	@RequestMapping(value = "staff", method = RequestMethod.GET)
	public ResponseEntity<?> saveStaff(@RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return staffService.getStaffBySchool(schoolId);
	}

	@PreAuthorize("hasPermission('staff', 'DELETE')")
	@RequestMapping(value = "staff", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteStaff(@RequestParam long staffId, @RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return staffService.deleteStaff(staffId, schoolId);
	}
}
