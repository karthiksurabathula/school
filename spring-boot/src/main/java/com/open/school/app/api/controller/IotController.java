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
import com.open.school.app.api.model.iot.ApplianceModel;
import com.open.school.app.api.service.IotServiceImpl;

@RestController
@RequestMapping("api/")
public class IotController {
	
	@Autowired
	private IotServiceImpl iotService;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;

	@PreAuthorize("hasPermission('appliance', 'POST')")
	@RequestMapping(value = "appliance", method = RequestMethod.POST)
	public ResponseEntity<?> saveAppliance(@RequestBody ApplianceModel appliance, @RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return iotService.createDevices(appliance,schoolId);
	}
	
	@PreAuthorize("hasPermission('appliance', 'GET')")
	@RequestMapping(value = "appliance", method = RequestMethod.GET)
	public ResponseEntity<?> getAppliances(@RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return iotService.getDevices(schoolId);
	}
}
