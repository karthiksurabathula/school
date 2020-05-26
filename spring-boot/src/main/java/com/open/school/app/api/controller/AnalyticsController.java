package com.open.school.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.open.school.app.api.authorization.CustomPermissionEvaluator;
import com.open.school.app.api.service.AnalyticsService;

@RestController
@RequestMapping("api/")
public class AnalyticsController {

	@Autowired
	private CustomPermissionEvaluator customPrevEval;
	@Autowired
	private AnalyticsService analytics;

	@RequestMapping(value = "analytics", method = RequestMethod.GET)
	public ResponseEntity<?> analytics(@RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
		return analytics.analytics(schoolId);
	}
}
