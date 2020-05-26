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
import com.open.school.app.api.model.timetable.TimeTableReq;
import com.open.school.app.api.service.TimeTableServiceImpl;

@RestController
@RequestMapping("api/")
public class TimeTableController {

	@Autowired
	private TimeTableServiceImpl timeTableServiceImpl;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;

	@PreAuthorize("hasPermission('timeTable', 'GET')")
	@RequestMapping(value = "timeTable", method = RequestMethod.GET)
	public ResponseEntity<?> getClasses(@RequestParam long schoolId, @RequestParam long classId, @RequestParam long sectionId, @RequestParam String day) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return timeTableServiceImpl.getTimeTableBySchoolClassSectionAndDay(schoolId, classId, sectionId, day);
	}
	
	@PreAuthorize("hasPermission('timeTableStaff', 'GET')")
	@RequestMapping(value = "timeTableStaff", method = RequestMethod.GET)
	public ResponseEntity<?> timetableStaff(@RequestParam long schoolId, @RequestParam String day) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return timeTableServiceImpl.getTimeTableByStaff(schoolId, day);
	}

	@PreAuthorize("hasPermission('timeTable', 'POST')")
	@RequestMapping(value = "timeTable", method = RequestMethod.POST)
	public ResponseEntity<?> saveTimeTable(@RequestBody TimeTableReq timeTableRequest, @RequestParam long schoolId, @RequestParam long classId, @RequestParam long sectionId, @RequestParam String day) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return timeTableServiceImpl.saveTimeTable(schoolId, classId, sectionId, day, timeTableRequest);
	}
}
