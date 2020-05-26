package com.open.school.app.api.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.SchoolCityEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.repository.SchoolRepository;
import com.open.school.app.jwt.entity.UserOrgEntity;
import com.open.school.app.jwt.repository.UserOrgRepository;
import com.open.school.app.jwt.service.JwtService;

@Service
public class SchoolServiceImpl {

	@Autowired
	private SchoolRepository schoolRepo;
	@Autowired
	private JwtService jwtServices;
	@Autowired
	private UserOrgRepository userOrgRepo;
	@Autowired
	private InitializationTrackerServiceImpl intiService;
	@Autowired
	private DeleteServiceLogic deleteService;
	
	private static final Logger log = LogManager.getLogger(PeriodServiceImpl.class);


	public ResponseEntity<?> getSchools(long cityId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			
			List<SchoolEntity> schoolObj = schoolRepo.getBySchoolByCityid(cityId);
			if (schoolObj.size() == 0) {
				response.put("message", "No Schools Found");
				response.put("indicator", "fail");
			} else {
				response.put("indicator", "success");
				response.put("school", schoolObj);
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

	public ResponseEntity<?> getSchoolsByUser() {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> school = new ArrayList<Object>();
		String message = null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			UserOrgEntity userOrg = userOrgRepo.findUserOrgMapByUsername(auth.getName());
			SchoolEntity schoolObj = schoolRepo.getBySchoolById(userOrg.getSchool().getId());
			if (schoolObj == null) {
				response.put("message", "No Schools Found");
				response.put("indicator", "fail");
			} else {
				school.add(schoolObj);
				response.put("school", school);
				response.put("indicator", "success");
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

	public ResponseEntity<?> saveSchool(SchoolEntity school, long cityId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			school.setStatus(true);
			school.setLastModified(new Date());
			school.setCreatedDate(new Date());
			school.setModifiedBy(auth.getName());
			school.setSchoolCity(new SchoolCityEntity(cityId, null, null, true, null, null, null, null));

			SchoolEntity newSchool = schoolRepo.saveAndFlush(school);
			response.put("school", newSchool);
			response.put("indicator", "success");
			response.put("message", school.getSchoolName() + " at " + school.getLocation() + " Created successfully");

			jwtServices.createAdminUser(RandomStringUtils.randomAlphanumeric(10), true, newSchool.getId());
			intiService.initializeClass(newSchool.getId(),false);
			
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

	public ResponseEntity<?> updateSchool(SchoolEntity school) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			Optional<SchoolEntity> actSchool = schoolRepo.findById(school.getId());
			if (actSchool == null) {
				response.put("indicator", "fail");
				response.put("code", "E1007");
				response.put("message", "School Not found");
			} else {
				school.setLastModified(new Date());
				school.setCreatedDate(actSchool.get().getCreatedDate());
				school.setSchoolCity(new SchoolCityEntity(actSchool.get().getSchoolCity().getId(), null, null, true, null, null, null, null));
				school.setModifiedBy(auth.getName());
				response.put("indicator", "success");
				response.put("school", schoolRepo.saveAndFlush(school));
				response.put("message", school.getSchoolName() + " at " + school.getLocation() + " updated successfully");
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

	public ResponseEntity<?> removeSchool(long id) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			Optional<SchoolEntity> school = schoolRepo.findById(id);
			if (school == null) {
				response.put("indicator", "fail");
				response.put("code", "E1007");
				response.put("message", "School Not found");
			} else {
//				response.put("school", school.get());
//				schoolRepo.delete(school.get());
				deleteService.deleteSchool(id);
				response.put("indicator", "success");
				response.put("message", school.get().getSchoolName() + " at " + school.get().getLocation() + " Deleted successfully");
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

	public ResponseEntity<?> getSchoolsById(long schoolId) {
	
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			
			SchoolEntity schoolObj = schoolRepo.getOne(schoolId);
			if (schoolObj == null) {
				response.put("indicator", "fail");
				response.put("message", "No Schools Found");
			} else {
				response.put("indicator", "success");
				response.put("school", schoolObj);	
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
}
