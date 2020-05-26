package com.open.school.app.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.open.school.app.api.authorization.CustomPermissionEvaluator;
import com.open.school.app.api.entity.AttendanceEntity;
import com.open.school.app.api.model.attendance.AttendanceReqModel;
import com.open.school.app.api.service.AttendanceServiceImpl;
import com.open.school.app.api.service.AttendanceTrackerServiceImpl;

@RestController
@RequestMapping("api/")
public class AttendanceController {

	@Autowired
	private AttendanceServiceImpl attendenceService;
	@Autowired
	private CustomPermissionEvaluator customPrevEval;
	@Autowired
	private AttendanceTrackerServiceImpl attendanceTrackerService;
	
	@PreAuthorize("hasPermission('attendance', 'GET')")
	@RequestMapping(value = "attendance", method = RequestMethod.GET)
	public ResponseEntity<?> getAttendance(@RequestParam long schoolId, @RequestParam long classId, @RequestParam long sectionId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return attendenceService.getAttendanceBySchoolClassSection(schoolId, classId, sectionId);
	}
	
	
	@PreAuthorize("hasPermission('absenteesBySchool', 'GET')")
	@RequestMapping(value = "absenteesBySchool", method = RequestMethod.GET)
	public ResponseEntity<?> getAbsenteesBySchool(@RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return attendenceService.getAbsenteesBySchool(schoolId);
	}
	
	
	@PreAuthorize("hasPermission('absenteesByStudent', 'GET')")
	@RequestMapping(value = "absenteesByStudent", method = RequestMethod.GET)
	public ResponseEntity<?> getAbsenteesByStudent(@RequestParam long schoolId, @RequestParam long studentId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return attendenceService.getAbsenteesBySchoolAndStudent(schoolId,studentId);
	}
	
	@PreAuthorize("hasPermission('absenteesByStudentId', 'GET')")
	@RequestMapping(value = "absenteesByStudentId", method = RequestMethod.GET)
	public ResponseEntity<?> getAbsenteesByStudentId(@RequestParam long schoolId, @RequestParam String studentId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return attendenceService.getAbsenteesBySchoolAndStudentId(schoolId,studentId);
	}
	
	@PreAuthorize("hasPermission('attendance', 'POST')")
	@RequestMapping(value = "attendance", method = RequestMethod.POST)
	public ResponseEntity<?> saveAttendance(@RequestBody List<AttendanceReqModel> attendance, @RequestParam long schoolId, @RequestParam long classId, @RequestParam long sectionId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return attendenceService.createAttendanceBySchoolClassSection(schoolId, classId, sectionId, attendance);
	}
	
	@PreAuthorize("hasPermission('attendanceNote', 'POST')")
	@RequestMapping(value = "attendanceNote", method = RequestMethod.POST)
	public ResponseEntity<?> saveAttendanceNotes(@RequestBody AttendanceEntity attendance, @RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return attendenceService.saveAbsenteesNote(schoolId, attendance);
	}
	
	@PreAuthorize("hasPermission('workingDay', 'POST')")
	@RequestMapping(value = "workingDay", method = RequestMethod.POST)
	public ResponseEntity<?> initializeWorkingDay(@RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return attendenceService.initializeWorkingday(schoolId);
	}
	
	@PreAuthorize("hasPermission('workingDay', 'GET')")
	@RequestMapping(value = "workingDay", method = RequestMethod.GET)
	public ResponseEntity<?> initializeWorkingDayCheck(@RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return attendenceService.initializeWorkingdayCheck(schoolId);
	}
	
	@PreAuthorize("hasPermission('attendanceStatus', 'GET')")
	@RequestMapping(value = "attendanceStatus", method = RequestMethod.GET)
	public ResponseEntity<?> getAbsenteesStatusBySchool(@RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return attendanceTrackerService.getAttendanceStatus(schoolId);
	}
	
}
