package com.open.school.app.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.MarksStatusTrackerEntity;
import com.open.school.app.api.repository.MarksStatusTrackerRepository;

@Service
public class MarksStatusTrackerImpl {

	@Autowired
	private MarksStatusTrackerRepository marksStatusRepo;
	
	private static final Logger log = LogManager.getLogger(MarksStatusTrackerImpl.class);

	public ResponseEntity<?> getTimeTableBySchoolClassAndExam(long schoolId, long examId) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> examMarksStatus = new ArrayList<Object>();
		String message = null;
		try {
			List<MarksStatusTrackerEntity> marksStatus = marksStatusRepo.getSubjectMarksStatusBySchoolAndExam(schoolId, examId);

			for (int i = 0; i < marksStatus.size(); i++) {
				HashMap<String, Object> examMarksStatusTemp = new HashMap<>();
				examMarksStatusTemp.put("subject", marksStatus.get(i).getSubject());
				examMarksStatusTemp.put("class", marksStatus.get(i).getClasses());
				examMarksStatusTemp.put("section", marksStatus.get(i).getSection());
				examMarksStatusTemp.put("subjectStatus", marksStatus.get(i));
				examMarksStatus.add(examMarksStatusTemp);
			}
			
			response.put("examMarksStatus",examMarksStatus);
			response.put("indicator", "success");
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1001");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
