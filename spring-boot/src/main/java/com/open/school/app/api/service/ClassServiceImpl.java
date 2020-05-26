package com.open.school.app.api.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.ClassEntity;
import com.open.school.app.api.entity.InitializationTrackerEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.repository.ClassRepository;
import com.open.school.app.api.repository.InitializationTrackerRepository;

@Service
public class ClassServiceImpl {

	@Autowired
	private ClassRepository classrepo;
	@Autowired
	private InitializationTrackerRepository initiRepo;
	@Autowired
	private DeleteServiceLogic deleteService;
	
	private static final Logger log = LogManager.getLogger(ClassServiceImpl.class);

	public ResponseEntity<?> getClasses(long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			
			List<ClassEntity> classObj = classrepo.getClassessBySchool(schoolId);
			if (classObj.size() == 0) {
				response.put("message", "No Classes Found");
				response.put("indicator", "fail");
			} else {
				response.put("indicator", "success");
				response.put("class", classObj);
			}
			
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

	public ResponseEntity<?> saveClass(ClassEntity classs, long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			classs.setStatus(true);
			classs.setLastModified(new Date());
			classs.setCreatedDate(new Date());
			classs.setModifiedBy(auth.getName());
			classs.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
			response.put("indicator", "success");
			response.put("class", classrepo.saveAndFlush(classs));
			response.put("message", classs.getClassName() + " Created successfully");
			InitializationTrackerEntity initTracker = initiRepo.getMarksBySclStuExamSubj(schoolId, "class");
			initTracker.setStatus(true);
			initTracker.setLastModified(new Date());
			initTracker.setModifiedBy(auth.getName());
			initiRepo.saveAndFlush(initTracker);
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

	public ResponseEntity<?> updateClass(ClassEntity classs, long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			Optional<ClassEntity> actClass = classrepo.findById(classs.getId());
			if (actClass == null) {
				response.put("indicator", "fail");
				response.put("code", "E1012");
				response.put("message", "Class Not found");
			} else {
				classs.setLastModified(new Date());
				classs.setCreatedDate(actClass.get().getCreatedDate());
				classs.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
				classs.setModifiedBy(auth.getName());
				response.put("indicator", "success");
				response.put("school", classrepo.saveAndFlush(classs));
				response.put("message", classs.getClassName() + " updated successfully");
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

	public ResponseEntity<?> removeClass(long schoolId, long classid) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			ClassEntity actClass = classrepo.getClassBySchoolAndClassId(schoolId, classid);
			if (actClass == null) {
				response.put("indicator", "fail");
				response.put("code", "E1014");
				response.put("message", "Class Not found");
			} else {
				response.put("school", actClass);
//				classrepo.delete(actClass);
				deleteService.deleteClasss(schoolId, classid);
				response.put("indicator", "success");
				response.put("message", actClass.getClassName() + " Deleted successfully");
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
