package com.open.school.app.jwt.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.open.school.app.api.authorization.CustomPermissionEvaluator;
import com.open.school.app.jwt.model.JwtRequest;
import com.open.school.app.jwt.model.ResetPasswordRequest;
import com.open.school.app.jwt.model.UserSettingRequest;
import com.open.school.app.jwt.service.JwtService;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> authenticate(@RequestBody JwtRequest authenticationRequest, HttpServletResponse responseMap) {
		return jwtService.authorize(authenticationRequest, responseMap);
	}

//	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> register(@RequestBody JwtRequest user, HttpServletResponse responseMap) {
		return jwtService.save(user, responseMap);
	}

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetReq, HttpServletResponse responseMap) {
		return jwtService.resetPassword(resetReq, responseMap);
	}

	@RequestMapping(value = "/api/reset", method = RequestMethod.POST)
	public ResponseEntity<?> resetPasswordLoggedIn(@RequestBody ResetPasswordRequest resetReq, HttpServletResponse responseMap) {
		return jwtService.resetPasswordLoggedIn(resetReq, responseMap);
	}

	@RequestMapping(value = "/api/logout", method = RequestMethod.POST)
	public ResponseEntity<?> resetPassword() {
		return jwtService.logout();
	}

	@PreAuthorize("hasPermission('users', 'GET')")
	@RequestMapping(value = "/api/users", method = RequestMethod.GET)
	public ResponseEntity<?> resetPassword(@RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return jwtService.getUsers(schoolId);
	}

	@PreAuthorize("hasPermission('userSettings', 'POST')")
	@RequestMapping(value = "/api/userSettings", method = RequestMethod.POST)
	public ResponseEntity<?> resetPassword(@RequestBody UserSettingRequest user, long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return jwtService.saveUserAccountChanges(user, schoolId);
	}

	@PreAuthorize("hasPermission('resetPassword', 'POST')")
	@RequestMapping(value = "/api/resetPassword", method = RequestMethod.POST)
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasss, long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return jwtService.resetUserPassword(resetPasss, schoolId);
	}

	@RequestMapping(value = "/api/authorize", method = RequestMethod.GET)
	public Authentication checkUserAuthenication() {
		return jwtService.checkUserAuthentication();
	}

}