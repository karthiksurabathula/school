package com.open.school.app.api.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.InitializationTrackerEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.entity.SubjectEntity;
import com.open.school.app.api.repository.InitializationTrackerRepository;
import com.open.school.app.api.repository.SubjectRepository;

@Service
public class SubjectServiceImpl {
	@Autowired
	private SubjectRepository subjectRepo;
	@Autowired
	private InitializationTrackerRepository initiRepo;
	@Autowired
	private DeleteServiceLogic deleteLogicService;
	
	private static final Logger log = LogManager.getLogger(SubjectServiceImpl.class);

	public ResponseEntity<?> getSubjects(long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			response.put("indicator", "success");
			List<SubjectEntity> subjObj = subjectRepo.getSubjectsBySchool(schoolId);
			if (subjObj.size() == 0) {
				response.put("message", "No Subjects found");
			}
			response.put("subject", subjObj);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1011");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> saveSubject(SubjectEntity subject, long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;

		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			subject.setLastModified(new Date());
			subject.setCreatedDate(new Date());
			subject.setModifiedBy(auth.getName());
			subject.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
			response.put("indicator", "success");
			response.put("subject", subjectRepo.saveAndFlush(subject));
			InitializationTrackerEntity initTracker = initiRepo.getMarksBySclStuExamSubj(schoolId, "subject");
			initTracker.setStatus(true);
			initiRepo.saveAndFlush(initTracker);
			response.put("message", subject.getSubjectName() + " Created successfully");

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

	public ResponseEntity<?> updateSubject(SubjectEntity subject, long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;

		try {
			SubjectEntity actSubj = subjectRepo.getSubjectBySchool(schoolId, subject.getId());
			if (actSubj == null) {
				response.put("indicator", "fail");
				response.put("code", "E1012");
				response.put("message", "Subject Not found");
			} else {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				subject.setLastModified(new Date());
				subject.setCreatedDate(actSubj.getCreatedDate());
				subject.setModifiedBy(auth.getName());
				subject.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
				response.put("indicator", "success");
				response.put("subject", subjectRepo.saveAndFlush(subject));
				response.put("message", subject.getSubjectName() + " updated successfully");
			}

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

	public ResponseEntity<?> removeSubject(long schoolId, long subjectId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			SubjectEntity actSubj = subjectRepo.getSubjectBySchool(schoolId, subjectId);
			if (actSubj == null) {
				response.put("indicator", "fail");
				response.put("code", "E1014");
				response.put("message", "Subject Not found");
			} else {
				//subjectRepo.delete(actSubj);
				deleteLogicService.deleteSubject(schoolId, actSubj.getId());
				response.put("indicator", "success");
				response.put("message", actSubj.getSubjectName() + " Deleted successfully");
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1015");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
