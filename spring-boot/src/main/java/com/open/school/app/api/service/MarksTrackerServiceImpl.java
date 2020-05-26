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

import com.open.school.app.api.entity.MarksTrackerEntity;
import com.open.school.app.api.entity.StudentEntity;
import com.open.school.app.api.entity.StudentMapEntity;
import com.open.school.app.api.repository.MarksTrackerRepository;
import com.open.school.app.api.repository.StudentMapRepository;

@Service
public class MarksTrackerServiceImpl {

	@Autowired
	private MarksTrackerRepository marksTrackerRepo;
	@Autowired
	private StudentMapRepository studentMapRepository;

	private static final Logger log = LogManager.getLogger(MarksTrackerServiceImpl.class);
	
	public ResponseEntity<?> getMarksByClass(long schoolId, long classId, long examId) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> marksResp = new ArrayList<Object>();

		String message = null;
		try {
			List<Long> studentIds = new ArrayList<Long>();
			List<StudentMapEntity> studentsMap = studentMapRepository.getByStudentsByClassId(classId, schoolId);
			if (studentsMap.size() > 0) {		
				for (int i = 0; i < studentsMap.size(); i++) {
					studentIds.add(studentsMap.get(i).getStudent().getId());
				}
			}
			List<MarksTrackerEntity> marks = marksTrackerRepo.getMarksByClassAndExam(schoolId, examId, studentIds);
			if(marks.size()>0) {
				for(int i=0;i<marks.size();i++) {
					HashMap<String, Object> marksRespNew = new HashMap<>();
					marksRespNew.put("rank", i+1);
					marksRespNew.put("marks", marks.get(i));
					StudentEntity stu = marks.get(i).getStudent();
					marksRespNew.put("student", new StudentEntity(stu.getId(),stu.getStudentId(),stu.getName(), null, null, null, null, null, null, null, null, null, null, null, false, true, null, null, null, null, null));
					marksResp.add(marksRespNew);
				}
				response.put("data", marksResp);
				response.put("indicator", "success");	
			} else {
				response.put("indicator", "fail");
				response.put("message", "Marks not found");
			}
			
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
