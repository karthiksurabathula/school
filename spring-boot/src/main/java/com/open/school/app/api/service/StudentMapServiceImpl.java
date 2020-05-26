package com.open.school.app.api.service;

import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.ClassEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.entity.SectionEntity;
import com.open.school.app.api.entity.StudentEntity;
import com.open.school.app.api.entity.StudentMapEntity;
import com.open.school.app.api.model.CustomRequest;
import com.open.school.app.api.repository.StudentMapRepository;

@Service
public class StudentMapServiceImpl {

	@Autowired
	private StudentServiceImpl studentService;
	@Autowired
	private StudentMapRepository studentMapRepository;
	private static final Logger log = LogManager.getLogger(StaffServiceImpl.class);


	public ResponseEntity<?> createMap(CustomRequest request) {

		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			StudentMapEntity studentMap = studentMapRepository.getByStudentId(request.getStudentId(), request.getSchoolId());
			if (studentMap != null) {
				studentMap.setClasses(new ClassEntity(request.getClassId(), null, null, null, null, true, null, null));
				studentMap.setSection(new SectionEntity(request.getSectionId(), null, true, null, null, null, null, null, null));
				studentMap.setModifiedBy(auth.getName());
				studentMap.setLastModified(new Date());
				studentMapRepository.saveAndFlush(studentMap);
				response.put("message", "Student Mapped Successfully");
			} else {
				StudentMapEntity studentMapNew = new StudentMapEntity();
				studentMapNew.setSchool(new SchoolEntity(request.getSchoolId(), null, null, null, null, null, null, true, null, null, null, null, null, null));
				studentMapNew.setClasses(new ClassEntity(request.getClassId(), null, null, null, null, true, null, null));
				studentMapNew.setSection(new SectionEntity(request.getSectionId(), null, true, null, null, null, null, null, null));
				studentMapNew.setStudent(new StudentEntity(request.getStudentId(), null, null, null, null, null, null, null, null, null, null, null, null, null, true, true, null, null, null, null, null));
				studentMapNew.setModifiedBy(auth.getName());
				studentMapNew.setLastModified(new Date());
				studentMapNew.setCreatedDate(new Date());
				studentMapRepository.saveAndFlush(studentMapNew);
				response.put("message", "Student Updated Successfully");
			}
			studentService.setClassMapPending(request.getStudentId());
			response.put("indicator", "success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1005");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> getSudentMap(long schoolId, long studentId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			StudentMapEntity studentMap = studentMapRepository.getByStudentId(studentId, schoolId);
			if (studentMap != null) {
				CustomRequest studentMapNew = new CustomRequest();
				studentMapNew.setSchoolId(studentMap.getSchool().getId());
				studentMapNew.setClassId(studentMap.getClasses().getId());
				studentMapNew.setSectionId(studentMap.getSection().getId());
				studentMapNew.setStudentId(studentId);
				response.put("indicator", "success");
				response.put("studentMap", studentMapNew);
			} else {
				response.put("indicator", "fail");
				response.put("message", "Student map not found");
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1005");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
