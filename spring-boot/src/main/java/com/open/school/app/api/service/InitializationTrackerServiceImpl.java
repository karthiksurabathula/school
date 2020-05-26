package com.open.school.app.api.service;

import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.InitializationTrackerEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.repository.InitializationTrackerRepository;

@Service
public class InitializationTrackerServiceImpl {

	@Autowired
	private InitializationTrackerRepository initiRepo;
	@Value("${url.school}")
	private String school;
	@Value("${url.class}")
	private String classUrl;
	@Value("${url.section}")
	private String section;
	@Value("${url.period}")
	private String period;
	@Value("${url.subject}")
	private String subject;
	@Value("${url.subjectClass}")
	private String subjectClass;
	@Value("${url.staff}")
	private String staff;
	@Value("${url.sampleData}")
	private String sampleData;
	@Value("${url.maintenance}")
	private String maintenance;
	

	private static final Logger log = LogManager.getLogger(InitializationTrackerServiceImpl.class);

	@Async("asyncExecutor")
	public void initializeClass(long schoolId, boolean status) {
		initiRepo.saveAndFlush(new InitializationTrackerEntity(0, "class", status, new Date(), "automated-job", new SchoolEntity(schoolId, null, null, null, null, null, null, false, null, null, null, null, null, null)));
		initiRepo.saveAndFlush(new InitializationTrackerEntity(0, "section", status, new Date(), "automated-job", new SchoolEntity(schoolId, null, null, null, null, null, null, false, null, null, null, null, null, null)));
		initiRepo.saveAndFlush(new InitializationTrackerEntity(0, "period", status, new Date(), "automated-job", new SchoolEntity(schoolId, null, null, null, null, null, null, false, null, null, null, null, null, null)));
		initiRepo.saveAndFlush(new InitializationTrackerEntity(0, "staff", status, new Date(), "automated-job", new SchoolEntity(schoolId, null, null, null, null, null, null, false, null, null, null, null, null, null)));
		initiRepo.saveAndFlush(new InitializationTrackerEntity(0, "subject", status, new Date(), "automated-job", new SchoolEntity(schoolId, null, null, null, null, null, null, false, null, null, null, null, null, null)));
		initiRepo.saveAndFlush(new InitializationTrackerEntity(0, "subjectClass", status, new Date(), "automated-job", new SchoolEntity(schoolId, null, null, null, null, null, null, false, null, null, null, null, null, null)));
	}

	@Async("asyncExecutor")
	public void initializeData() {
		InitializationTrackerEntity initTracker = initiRepo.getMarksBySclStuExamSubj("datacreation");
		if (initTracker == null) {
			initiRepo.saveAndFlush(new InitializationTrackerEntity(0, "datacreation", false, new Date(), "automated-job", null));
		}
	}

	public ResponseEntity<?> getApplicationStatus(long schoolId) {

		HashMap<String, Object> response = new HashMap<>();
		try {

			InitializationTrackerEntity initTracker = initiRepo.getMarksBySclStuExamSubj(schoolId, "class");
			if (!initTracker.isStatus()) {
				response.put("indicator", "fail");
				response.put("redirecturl", classUrl);
				response.put("message", "Classes not configured");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("indicator", "success");
			}

			initTracker = initiRepo.getMarksBySclStuExamSubj(schoolId, "section");
			if (!initTracker.isStatus()) {
				response.put("indicator", "fail");
				response.put("redirecturl", section);
				response.put("message", "Section not configured");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("indicator", "success");
			}

			initTracker = initiRepo.getMarksBySclStuExamSubj(schoolId, "period");
			if (!initTracker.isStatus()) {
				response.put("indicator", "fail");
				response.put("redirecturl", period);
				response.put("message", "Period not configured");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("indicator", "success");
			}

			initTracker = initiRepo.getMarksBySclStuExamSubj(schoolId, "subject");
			if (!initTracker.isStatus()) {
				response.put("indicator", "fail");
				response.put("redirecturl", subject);
				response.put("message", "Subject not configured");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("indicator", "success");
			}

			initTracker = initiRepo.getMarksBySclStuExamSubj(schoolId, "staff");
			if (!initTracker.isStatus()) {
				response.put("indicator", "fail");
				response.put("redirecturl", staff);
				response.put("message", "Subject not configured");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("indicator", "success");
			}

			initTracker = initiRepo.getMarksBySclStuExamSubj(schoolId, "subjectClass");
			if (!initTracker.isStatus()) {
				response.put("indicator", "fail");
				response.put("redirecturl", subjectClass);
				response.put("message", "Subject Class Map not configured");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("indicator", "success");
			}

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1011");
			response.put("message", "Error Occured, if issue persists please contact administrator");
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public ResponseEntity<?> getAppStatusAdmin() {
		HashMap<String, Object> response = new HashMap<>();
		try {

			InitializationTrackerEntity initTracker = initiRepo.getMarksBySclStuExamSubj("datacreation");
			if (initTracker.isStatus()) {
				response.put("indicator", "success");
				response.put("redirecturl", sampleData);
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1011");
			response.put("message", "Error Occured, if issue persists please contact administrator");
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
