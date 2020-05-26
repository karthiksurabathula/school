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

import com.open.school.app.api.entity.SchoolCityEntity;
import com.open.school.app.api.repository.SchoolCityRepository;

@Service
public class SchoolCityServiceImpl {

	@Autowired
	private SchoolCityRepository schoolCityRepo;
	@Autowired
	private DeleteServiceLogic deleteService;
	
	private static final Logger log = LogManager.getLogger(PeriodServiceImpl.class);

	public ResponseEntity<?> getCities() {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			
			List<SchoolCityEntity> schoolCityObj = schoolCityRepo.findAll();
			if (schoolCityObj.size() == 0) {
				response.put("message", "No Cities Found");
				response.put("indicator", "fail");
			} else {
				response.put("indicator", "success");
				response.put("city", schoolCityObj);
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

	public ResponseEntity<?> saveCity(SchoolCityEntity schoolCity) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			schoolCity.setStatus(true);
			schoolCity.setLastModified(new Date());
			schoolCity.setCreatedDate(new Date());
			schoolCity.setModifiedBy(auth.getName());
			response.put("city", schoolCityRepo.saveAndFlush(schoolCity));
			response.put("indicator", "success");
			response.put("message", schoolCity.getCity() + " Created successfully");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1002");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> updateCity(SchoolCityEntity schoolCity) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			Optional<SchoolCityEntity> city = schoolCityRepo.findById(schoolCity.getId());
			if (city == null) {
				response.put("indicator", "fail");
				response.put("code", "E1009");
				response.put("message", "City Not found");
			} else {
				city.get().setStatus(schoolCity.isStatus());
				city.get().setLastModified(new Date());
				city.get().setCity(schoolCity.getCity());
				city.get().setState(schoolCity.getState());
				city.get().setModifiedBy(auth.getName());
				response.put("city", schoolCityRepo.saveAndFlush(city.get()));
				response.put("indicator", "success");
				response.put("message", schoolCity.getCity() + " updated successfully");
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1002");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> removeCity(long id) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			Optional<SchoolCityEntity> city = schoolCityRepo.findById(id);
			if (city == null) {
				response.put("indicator", "fail");
				response.put("code", "E1008");
				response.put("message", "City Not found");
			} else {
				response.put("city", city.get());
//				schoolCityRepo.delete(city.get());
				deleteService.deleteCity(id);
				response.put("indicator", "success");
				response.put("message", city.get().getCity() + " Deleted successfully");
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1003");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
