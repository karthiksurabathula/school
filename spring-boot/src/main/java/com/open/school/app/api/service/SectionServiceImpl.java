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
import com.open.school.app.api.entity.SectionEntity;
import com.open.school.app.api.repository.InitializationTrackerRepository;
import com.open.school.app.api.repository.SectionRepository;

@Service
public class SectionServiceImpl {

	@Autowired
	private SectionRepository sectionRepo;
	@Autowired
	private InitializationTrackerRepository initiRepo;
	@Autowired
	private DeleteServiceLogic deleteService;
	
	private static final Logger log = LogManager.getLogger(SectionServiceImpl.class);

	public ResponseEntity<?> getSectionBySchoolAndClass(long schoolId, long classId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			
			List<SectionEntity> sectionObj = sectionRepo.getBySectionsBySchoolAndClass(schoolId, classId);
			if (sectionObj.size() == 0) {
				response.put("message", "No Sections Found");
				response.put("indicator", "fail");
			} else {
				response.put("indicator", "success");
				response.put("section", sectionObj);
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1004");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> saveSection(SectionEntity section, long schoolId, long classId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			section.setStatus(true);
			section.setLastModified(new Date());
			section.setCreatedDate(new Date());
			section.setModifiedBy(auth.getName());
			section.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
			section.setClasses(new ClassEntity(classId, null, null, null, null, true, null, null));
			response.put("indicator", "success");
			response.put("section", sectionRepo.saveAndFlush(section));
			InitializationTrackerEntity initTracker = initiRepo.getMarksBySclStuExamSubj(schoolId, "section");
			initTracker.setStatus(true);
			initiRepo.saveAndFlush(initTracker);
			response.put("message", section.getSectionName() + " Created successfully");

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

	public ResponseEntity<?> updateSection(SectionEntity section, long schoolId, long classId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			Optional<SectionEntity> actSection = sectionRepo.findById(section.getId());
			if (actSection == null) {
				response.put("indicator", "fail");
				response.put("code", "E1007");
				response.put("message", "Section Not found");
			} else {
				section.setLastModified(new Date());
				section.setCreatedDate(actSection.get().getCreatedDate());
				section.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
				section.setClasses(new ClassEntity(classId, null, null, null, null, true, null, null));
				section.setModifiedBy(auth.getName());
				response.put("indicator", "success");
				response.put("section", sectionRepo.saveAndFlush(section));
				response.put("message", section.getSectionName() + " updated successfully");
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

	public ResponseEntity<?> removeSection(long schoolId, long sectionId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			SectionEntity actSection = sectionRepo.getBySectionsBySchoolAndSection(schoolId, sectionId);
			if (actSection == null) {
				response.put("indicator", "fail");
				response.put("code", "E1007");
				response.put("message", "Section Not found");
			} else {
				response.put("section", actSection);
				deleteService.deleteSection(sectionId, schoolId);
//				sectionRepo.delete(actSection);
				response.put("indicator", "success");
				response.put("message", actSection.getSectionName() + " Deleted successfully");
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1006");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
