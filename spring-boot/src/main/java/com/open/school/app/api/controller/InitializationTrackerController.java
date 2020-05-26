package com.open.school.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.open.school.app.api.service.InitializationTrackerServiceImpl;

@RestController
@RequestMapping("api/")
public class InitializationTrackerController {

	@Autowired
	private InitializationTrackerServiceImpl initService;

	@RequestMapping(value = "appStatus", method = RequestMethod.GET)
	public ResponseEntity<?> getAppStatus(@RequestParam long schoolId) {
		return initService.getApplicationStatus(schoolId);
	}
	
	@RequestMapping(value = "appStatusAdmin", method = RequestMethod.GET)
	public ResponseEntity<?> getAppStatusAdmin() {
		return initService.getAppStatusAdmin();
	}

}
