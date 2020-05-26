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
import com.open.school.app.api.model.period.PeriodReqModel;
import com.open.school.app.api.service.PeriodServiceImpl;

@RestController
@RequestMapping("api/")
public class PeriodController {

	@Autowired
	private PeriodServiceImpl periodServiceImpl;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;

	@PreAuthorize("hasPermission('period', 'POST')")
	@RequestMapping(value = "period", method = RequestMethod.POST)
	public ResponseEntity<?> savePeriod(@RequestBody PeriodReqModel period, @RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return periodServiceImpl.savePeriod(period, schoolId);
	}

	@PreAuthorize("hasPermission('period', 'GET')")
	@RequestMapping(value = "period", method = RequestMethod.GET)
	public ResponseEntity<?> getPeriodsByClass(@RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return periodServiceImpl.getPeriodBySchool(schoolId);
	}

	@PreAuthorize("hasPermission('period', 'DELETE')")
	@RequestMapping(value = "period", method = RequestMethod.DELETE)
	public ResponseEntity<?> getPeriodsByClass(@RequestParam long schoolId, @RequestParam long periodId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return periodServiceImpl.deletePeriod(schoolId, periodId);
	}
}
