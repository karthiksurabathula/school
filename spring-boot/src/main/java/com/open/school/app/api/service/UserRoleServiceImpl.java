package com.open.school.app.api.service;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.StaffEntity;
import com.open.school.app.api.repository.StaffRepository;
import com.open.school.app.jwt.entity.UserRoleEntity;
import com.open.school.app.jwt.repository.UserRoleRepository;

@Service
public class UserRoleServiceImpl {

	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private StaffRepository staffRepo;

	private static final Logger log = LogManager.getLogger(UserRoleServiceImpl.class);
	
	public ResponseEntity<?> getRoles() {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			
			List<UserRoleEntity> userRoles = userRoleRepository.getUserRoles();
			if (userRoles.size() == 0) {
				response.put("message", "User Roles Not found");
				response.put("indicator", "fail");
			} else {
				response.put("indicator", "success");
				response.put("roles", userRoles);
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

	public ResponseEntity<?> getRolesByStaffId(long schoolId, long staffId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			StaffEntity staff = staffRepo.getByStaffBySchoolAndId(schoolId, staffId);
			if (staff == null) {
				response.put("indicator", "fail");
				response.put("code", "E1007");
				response.put("message", "Staff Not found");
			} else {
				response.put("indicator", "success");
				response.put("roles", staff.getRole());
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
