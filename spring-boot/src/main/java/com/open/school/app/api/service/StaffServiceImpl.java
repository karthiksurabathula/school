package com.open.school.app.api.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.entity.StaffEntity;
import com.open.school.app.api.repository.StaffRepository;
import com.open.school.app.jwt.entity.UserRoleEntity;
import com.open.school.app.jwt.service.JwtService;

@Service
public class StaffServiceImpl {

	@Autowired
	private StaffRepository staffRepo;
	@Autowired
	private JwtService jwsService;
	@Autowired
	private DeleteServiceLogic deleteService;

	private static final Logger log = LogManager.getLogger(StaffServiceImpl.class);

	public ResponseEntity<?> saveStaff(StaffEntity staff, long schoolId, String role) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {

			String staffId = "STF" + ((100000 * schoolId) + (staffRepo.getByStaffCountBySchoolAndId(schoolId) + 1));
			String pass = RandomStringUtils.randomAlphanumeric(10);

			staff.setStatus(true);
			staff.setLastModified(new Date());
			staff.setCreatedDate(new Date());
			staff.setModifiedBy(auth.getName());
			staff.setStaffId(staffId);
			staff.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
			staff.setRole(new UserRoleEntity(role, null));
			response.put("indicator", "success");
			response.put("staff", staffRepo.saveAndFlush(staff));
			response.put("message", staff.getName() + " Created successfully");
			jwsService.createUser(staffId, pass, role, true, schoolId);
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

	public ResponseEntity<?> updateStaff(StaffEntity staff, long schoolId, String role) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			StaffEntity actStaff = staffRepo.getByStaffBySchoolAndId(schoolId, staff.getId());
			if (actStaff == null) {
				response.put("indicator", "fail");
				response.put("code", "E1007");
				response.put("message", "Staff Not found");
			} else {
				staff.setStaffId(actStaff.getStaffId());
				staff.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
				staff.setRole(new UserRoleEntity(role, null));
				staff.setModifiedBy(auth.getName());
				staff.setCreatedDate(actStaff.getCreatedDate());
				staff.setLastModified(new Date());
				response.put("indicator", "success");
				response.put("staff", staffRepo.saveAndFlush(staff));
				response.put("message", staff.getName() + " updated successfully");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
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

	public ResponseEntity<?> getStaffBySchool(long schoolId) {

		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			List<StaffEntity> staff = staffRepo.getByStaffBySchool(schoolId);
			
			if (staff.size() == 0) {
				response.put("message", "No Staff found");
				response.put("indicator", "fail");
			} else {
				response.put("indicator", "success");
				response.put("staff", staff);
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

	public ResponseEntity<?> deleteStaff(long staffId, long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			StaffEntity actStaff = staffRepo.getByStaffBySchoolAndId(schoolId, staffId);
			if (actStaff == null) {
				response.put("indicator", "fail");
				response.put("code", "E1007");
				response.put("message", "Staff Not found");
			} else {
				deleteService.deleteStaff(staffId);
				response.put("indicator", "success");
				response.put("message", "Staff deleted successfully");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
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
