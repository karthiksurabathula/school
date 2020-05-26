package com.open.school.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.open.school.app.api.authorization.CustomPermissionEvaluator;
import com.open.school.app.api.service.MarksTrackerServiceImpl;

@RestController
@RequestMapping("api/")
public class MarksTrackerController {
	
	@Autowired
	private MarksTrackerServiceImpl marksTrackService;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;
	
	@PreAuthorize("hasPermission('marksByClass', 'GET')")
	@RequestMapping(value = "marksByClass", method = RequestMethod.GET)
	public ResponseEntity<?> getMarksByClass(@RequestParam long schoolId, @RequestParam long classId, @RequestParam long examId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return marksTrackService.getMarksByClass(schoolId, classId, examId);
	}
}

