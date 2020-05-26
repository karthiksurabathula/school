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
import com.open.school.app.api.entity.SubjectEntity;
import com.open.school.app.api.entity.SyllabusEntity;
import com.open.school.app.api.repository.SyllabusRepository;

@Service
public class SyllabusService {

	@Autowired
	private SyllabusRepository syllabusRepo;

	private static final Logger log = LogManager.getLogger(SyllabusService.class);

	
	public ResponseEntity<?> syllabusUpdate(long schoolId, long classId, long sectionId, long subjectId, SyllabusEntity syllabus) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			SyllabusEntity syllabusResp = syllabusRepo.getSyllabus(schoolId, syllabus.getId());
			if (syllabusResp == null) {
				syllabus.setLastModified(new Date());
				syllabus.setModifiedBy(auth.getName());
				syllabus.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
				syllabus.setClasses(new ClassEntity(classId, null, null, null, null, false, null, null));
				syllabus.setSection(new SectionEntity(sectionId, null, true, null, null, null, null, null, null));
				syllabus.setSubject(new SubjectEntity(subjectId, null, null, null, null, null, null));
				syllabusRepo.saveAndFlush(syllabus);
			} else {
				syllabusResp.setDescription(syllabus.getDescription());
				syllabusResp.setPercentage(syllabus.getPercentage());
				syllabusResp.setLastModified(new Date());
				syllabusResp.setModifiedBy(auth.getName());
				syllabusRepo.saveAndFlush(syllabusResp);
			}
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

	public ResponseEntity<?> getSyllabus(long schoolId, long classId, long sectionId, long subjectId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			SyllabusEntity syllabus = syllabusRepo.getSyllabusBySection(schoolId, classId, sectionId, subjectId);
			if (syllabus == null) {
				response.put("syllabus", new SyllabusEntity(0, "", 0, null, null, null, null, null, null));
			} else {
				response.put("syllabus", syllabus);
			}
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
