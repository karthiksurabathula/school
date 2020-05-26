package com.open.school.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.open.school.app.api.authorization.CustomPermissionEvaluator;
import com.open.school.app.api.service.MarksStatusTrackerImpl;

@RestController
@RequestMapping("api/")
public class MarksStatusTrackerController {

	@Autowired
	private MarksStatusTrackerImpl marksStatusImpl;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;

	@PreAuthorize("hasPermission('marksStatus', 'GET')")
	@RequestMapping(value = "marksStatus", method = RequestMethod.GET)
	public ResponseEntity<?> getMarksBySubject(@RequestParam long schoolId, @RequestParam long examId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return marksStatusImpl.getTimeTableBySchoolClassAndExam(schoolId, examId);
	}

}
