package com.open.school.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.open.school.app.api.authorization.CustomPermissionEvaluator;
import com.open.school.app.api.service.UserRoleServiceImpl;

@RestController
@RequestMapping("/api")
public class UserRolesController {

	@Autowired
	private UserRoleServiceImpl userRoleService;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;

	@PreAuthorize("hasPermission('role', 'GET')")
	@RequestMapping(value = "role", method = RequestMethod.GET)
	public ResponseEntity<?> getRoles() {
		return userRoleService.getRoles();
	}

	@PreAuthorize("hasPermission('roleByStaff', 'GET')")
	@RequestMapping(value = "role/staffId={id}", method = RequestMethod.GET)
	public ResponseEntity<?> getRoleByStaffId(@RequestParam long schoolId, @PathVariable("id") long staffId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return userRoleService.getRolesByStaffId(schoolId, staffId);
	}

}
