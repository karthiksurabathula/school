package com.open.school.app.dummyData;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.InitializationTrackerEntity;
import com.open.school.app.api.repository.InitializationTrackerRepository;

@Service
public class DummyDataService {

	@Autowired
	private DummyDataLogicService dummyDataLogic;
	@Autowired
	private InitializationTrackerRepository initiRepo;

	private static final Logger log = LogManager.getLogger(DummyDataService.class);

	public ResponseEntity<?> loadData(long cityCount, long schoolCount, long classCount, long sectionCount, long subjectCount, long staffCount, long studentCout, long periodCount) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			response.put("indicator", "success");
			response.put("message", "Data creation is in progress");
			dummyDataLogic.loadData(cityCount, schoolCount, classCount, sectionCount, subjectCount, staffCount, studentCout, periodCount);
			response.put("initTracker", new InitializationTrackerEntity(0, "datacreation", true, null, null, null));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1013");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> checkInitialization() {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			InitializationTrackerEntity initTracker = initiRepo.getMarksBySclStuExamSubj("datacreation");
			response.put("indicator", "success");
			response.put("initTracker", initTracker);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1013");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public ResponseEntity<?> loadDailyTasks(long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			response.put("indicator", "success");
			response.put("message", "Data creation is in progress");
			dummyDataLogic.loadDailyTasks(schoolId);
			response.put("initTracker", new InitializationTrackerEntity(0, "datacreation", true, null, null, null));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1013");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
