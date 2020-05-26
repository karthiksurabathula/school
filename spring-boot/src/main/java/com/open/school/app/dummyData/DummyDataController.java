package com.open.school.app.dummyData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class DummyDataController {

	@Autowired
	private DummyDataService dummyDataService;

	@PreAuthorize("hasPermission('dummyData', 'GET')")
	@RequestMapping(value = "dummyData", method = RequestMethod.GET)
	public ResponseEntity<?> dummyDataCheck() {
		return dummyDataService.checkInitialization();
	}

	@PreAuthorize("hasPermission('dummyData', 'POST')")
	@RequestMapping(value = "dummyData", method = RequestMethod.POST)
	public ResponseEntity<?> dummyData(@RequestParam long cityCount, @RequestParam long schoolCount, @RequestParam long classCount, @RequestParam long sectionCount, @RequestParam long subjectCount, @RequestParam long staffCount, @RequestParam long studentCout, @RequestParam long periodCount) {
		return dummyDataService.loadData(cityCount, schoolCount, classCount, sectionCount, subjectCount, staffCount, studentCout, periodCount);
	}
	
	@PreAuthorize("hasPermission('dummyData', 'PUT')")
	@RequestMapping(value = "dummyData", method = RequestMethod.PUT)
	public ResponseEntity<?> dummyData(@RequestParam long schoolId) {
		return dummyDataService.loadDailyTasks(schoolId);
	}
}
