package com.open.school.app.api.service;

import java.util.ArrayList;
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

import com.open.school.app.api.entity.ClassEntity;
import com.open.school.app.api.entity.InitializationTrackerEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.entity.SubjectClassMapEntity;
import com.open.school.app.api.entity.SubjectEntity;
import com.open.school.app.api.model.CustomRequest;
import com.open.school.app.api.repository.InitializationTrackerRepository;
import com.open.school.app.api.repository.SubjectClassMapRepository;

@Service
public class SubjectClassMapServiceImpl {

	@Autowired
	private SubjectClassMapRepository subjectClassMapRepository;
	@Autowired
	private InitializationTrackerRepository initiRepo;
	@Autowired
	private DeleteServiceLogic deleteService;

	private static final Logger log = LogManager.getLogger(SubjectClassMapServiceImpl.class);

	public ResponseEntity<?> createClassSubjectMap(CustomRequest request) {

		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			SubjectClassMapEntity subjectMapNew = new SubjectClassMapEntity();
			SubjectClassMapEntity subjectMap = subjectClassMapRepository.getSubjectAndClassMap(request.getSchoolId(), request.getClassId(), request.getSubjectId());
			if (subjectMap == null) {
				subjectMapNew.setSchool(new SchoolEntity(request.getSchoolId(), null, null, null, null, null, null, true, null, null, null, null, null, null));
				subjectMapNew.setClasses(new ClassEntity(request.getClassId(), null, null, null, null, true, null, null));
				subjectMapNew.setSubject(new SubjectEntity(request.getSubjectId(), null, null, null, null, null, null));
				subjectMapNew.setOptional(request.isOptional());
				subjectMapNew.setModifiedBy(auth.getName());
				subjectMapNew.setLastModified(new Date());
				subjectMapNew.setCreatedDate(new Date());
				subjectClassMapRepository.saveAndFlush(subjectMapNew);

				InitializationTrackerEntity initTracker = initiRepo.getMarksBySclStuExamSubj(request.getSchoolId(), "subjectClass");
				initTracker.setStatus(true);
				initiRepo.saveAndFlush(initTracker);

				response.put("message", "Subject Map created Successfully");
				response.put("indicator", "success");
			} else {
				response.put("message", "Subject already mapped to class");
				response.put("indicator", "fail");
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

	public ResponseEntity<?> getSubjectsBySchoolAndClass(long schoolId, long classId) {
		HashMap<String, Object> response = new HashMap<>();
		List<CustomRequest> subjectClassMapResults = new ArrayList<CustomRequest>();
		String message = null;

		try {

			List<Object[]> subjectsMap = subjectClassMapRepository.getSubjectsMappedToSchoolClass(schoolId, classId);

			if (subjectsMap == null) {
				response.put("message", "No Subjects Found");
				response.put("indicator", "fail");
			} else {
				for (Object[] result : subjectsMap) {
					subjectClassMapResults.add(new CustomRequest(schoolId, classId, 0, (long) result[3], 0, (boolean) result[1], (String) result[2], (long) result[0], 0));
				}
				response.put("subjectClassMap", subjectClassMapResults);
				if (!(subjectClassMapResults.size() > 0)) {
					response.put("message", "No Subjects Found");
					response.put("indicator", "fail");
				} else {
					response.put("indicator", "success");
				}
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

	public ResponseEntity<?> updateClassSubjectMap(CustomRequest request) {

		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			SubjectClassMapEntity subjectMap = subjectClassMapRepository.getSubjectAndClassMapById(request.getSubjectMapId(), request.getSchoolId());
			if (subjectMap != null) {
				subjectMap.setOptional(request.isOptional());
				subjectMap.setLastModified(new Date());
				subjectMap.setModifiedBy(auth.getName());
				subjectClassMapRepository.saveAndFlush(subjectMap);
				response.put("message", "Subject updated Successfully");
				response.put("indicator", "success");
			} else {
				response.put("message", "Subject not Mapped to class");
				response.put("indicator", "fail");
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

	public ResponseEntity<?> removeSubjectClassMap(long subjectMapId, long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			SubjectClassMapEntity subjectClassMap = subjectClassMapRepository.getSubjectAndClassMapById(subjectMapId, schoolId);
			if (subjectClassMap == null) {
				response.put("indicator", "fail");
				response.put("code", "E1014");
				response.put("message", "Class Not found");
			} else {
				deleteService.deleteSubjectClassMap(schoolId,subjectClassMap.getSubject().getId(),subjectClassMap.getClasses().getId() );
				response.put("indicator", "success");
				response.put("message", subjectClassMap.getSubject().getSubjectName() + " Deleted successfully");
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
