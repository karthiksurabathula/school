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
import com.open.school.app.api.entity.StaffEntity;
import com.open.school.app.api.entity.SubjectClassMapEntity;
import com.open.school.app.api.entity.SubjectEntity;
import com.open.school.app.api.entity.SubjectStaffMapEntity;
import com.open.school.app.api.repository.SubjectClassMapRepository;
import com.open.school.app.api.repository.SubjectStaffMapRepository;

@Service
public class SubjectStaffMapServiceImpl {

	@Autowired
	private SubjectStaffMapRepository subjectStaffMapRepository;
	@Autowired
	private SubjectClassMapRepository subjectClassMapRepository;
	
	private static final Logger log = LogManager.getLogger(SubjectStaffMapServiceImpl.class);

	public ResponseEntity<?> addSubjectStaffMap(long schoolId, long classId, long subjectId, long staffId, long sectionId) {

		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			SubjectStaffMapEntity subjectStaffMap = new SubjectStaffMapEntity();
			SubjectClassMapEntity subjectMap = subjectClassMapRepository.getSubjectAndClassMap(schoolId, classId, subjectId);
			if (subjectMap == null) {
				response.put("message", "Subject not assigned to Class");
				response.put("indicator", "fail");
			} else {
				SubjectStaffMapEntity result = subjectStaffMapRepository.getStaffBySubjectSchool(schoolId, subjectId,classId,sectionId);
				if (result == null) {
					subjectStaffMap.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
					subjectStaffMap.setSubject(new SubjectEntity(subjectId, null, null, null, null, null, null));
					subjectStaffMap.setStaff(new StaffEntity(staffId, null, null, null, null, null, null, null, null, null, null, null, true, null, null, null, null));
					subjectStaffMap.setClasses(new ClassEntity(classId, null, null, null, null, true, null, null));
					subjectStaffMap.setSection(new SectionEntity(sectionId, null, true, null, null, null, null, null, null));
					subjectStaffMap.setModifiedBy(auth.getName());
					subjectStaffMap.setLastModified(new Date());
					subjectStaffMap.setCreatedDate(new Date());
					subjectStaffMapRepository.saveAndFlush(subjectStaffMap);
					response.put("result", subjectStaffMap);
				} else {
					result.setStaff(new StaffEntity(staffId, null, null, null, null, null, null, null, null, null, null, null, true, null, null, null, null));
					result.setModifiedBy(auth.getName());
					result.setLastModified(new Date());
					subjectStaffMapRepository.saveAndFlush(result);
					response.put("result", result);
				}
				response.put("message", "Staff Map created Successfully");
				response.put("indicator", "success");
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

	public ResponseEntity<?> getSubjectStaffMap(long schoolId, long classId, long subjectId, long sectionId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			SubjectClassMapEntity subjectMap = subjectClassMapRepository.getSubjectAndClassMap(schoolId, classId, subjectId);
			if (subjectMap == null) {
				response.put("message", "Subject not assigned to Class");
				response.put("indicator", "fail");
			} else {
				Object result = subjectStaffMapRepository.getStaffBySubjectClassSectionSchool(schoolId, subjectId,classId,sectionId);
				if (result == null) {
					response.put("staffId", "disabled");
				} else {
					response.put("staffId", (long) result);
				}
				response.put("indicator", "success");
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
