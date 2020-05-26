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

import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.entity.StudentEntity;
import com.open.school.app.api.entity.StudentMapEntity;
import com.open.school.app.api.repository.StudentMapRepository;
import com.open.school.app.api.repository.StudentRepository;
import com.open.school.app.jwt.service.JwtService;

@Service
public class StudentServiceImpl {

	@Autowired
	private StudentRepository studentRepo;
	@Autowired
	private StudentMapRepository studentMapRepository;
	@Autowired
	private JwtService jwsService;
	@Autowired
	private DeleteServiceLogic deleteService;

	private static final Logger log = LogManager.getLogger(StudentServiceImpl.class);

	public ResponseEntity<?> saveStudent(StudentEntity student, long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			if (student.getDob().length() <= 0) {
				response.put("indicator", "fail");
				response.put("code", "E1007");
				response.put("message", "DOB Not Set");
			} else {
				String userId = "STU" + ((100000 * schoolId) + (studentRepo.getCountOfStudentsBySchool(schoolId) + 1));
				if (student.getStudentId().length() <= 0) {
					student.setStudentId(userId);
				} else {
					int no = studentRepo.getCoutOfStudentsIdByStudentIdAndSchool(student.getStudentId(), schoolId);
					if (no > 0) {
						response.put("indicator", "fail");
						response.put("code", "E1007");
						response.put("message", "Stundet ID is in use, please provide unique id");
						return new ResponseEntity<>(response, HttpStatus.OK);
					}
				}
				student.setStatus(true);
				student.setPending(true);
				student.setLastModified(new Date());
				student.setCreatedDate(new Date());
				student.setUsername(userId);
				student.setModifiedBy(auth.getName());
				student.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
				response.put("indicator", "success");
				response.put("student", studentRepo.saveAndFlush(student));
				response.put("message", student.getName() + " Created successfully");
				jwsService.createUser(userId, RandomStringUtils.randomAlphanumeric(10), "STUDENT", true, schoolId);
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

	public ResponseEntity<?> updateStudent(StudentEntity student, long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			Optional<StudentEntity> actStudent = studentRepo.findById(student.getId());
			if (actStudent == null) {
				response.put("indicator", "fail");
				response.put("code", "E1007");
				response.put("message", "Student Not found");
			} else {

				if (!student.getStudentId().equals(actStudent.get().getStudentId())) {
					int no = studentRepo.getCoutOfStudentsIdByStudentIdAndSchool(student.getStudentId(), schoolId);
					if (no > 0) {
						response.put("indicator", "fail");
						response.put("code", "E1007");
						response.put("message", "Stundet ID is in use, please provide unique id");
						return new ResponseEntity<>(response, HttpStatus.OK);
					}
				}

				student.setLastModified(new Date());
				student.setCreatedDate(actStudent.get().getCreatedDate());
				student.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
				student.setPending(actStudent.get().isPending());
				student.setModifiedBy(auth.getName());
				response.put("indicator", "success");
				response.put("section", studentRepo.saveAndFlush(student));
				response.put("message", student.getName() + " updated successfully");
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

	public ResponseEntity<?> removeStudent(long schoolId, long studentId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			StudentEntity actStudent = studentRepo.getByStudentIdBySchool(studentId, schoolId);
			if (actStudent == null) {
				response.put("indicator", "fail");
				response.put("code", "E1007");
				response.put("message", "Student Not found");
			} else {
				deleteService.deleteStudent(actStudent.getId());
				response.put("indicator", "success");
				response.put("message", actStudent.getName() + " Deleted successfully");
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

	public ResponseEntity<?> getStudentsByStudentId(long schoolId, String studentId) {
		HashMap<String, Object> response = new HashMap<>();
		List<StudentEntity> studentList = new ArrayList<StudentEntity>();
		String message = null;
		try {

			StudentEntity studentObj = studentRepo.getByStudentIdBySchool(studentId, schoolId);
			if (studentObj == null) {
				response.put("indicator", "fail");
				message = "Student with " + studentId + " Id not found";
				response.put("message", message);
			} else {
				response.put("indicator", "success");
				studentList.add(studentObj);
				response.put("student", studentList);
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

	public ResponseEntity<?> getStudentsbyClass(long schoolId, long classId) {

		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			List<StudentMapEntity> studentsMap = studentMapRepository.getByStudentsByClassId(classId, schoolId);
			if (studentsMap.size() > 0) {
				List<Long> studentIds = new ArrayList<Long>();
				for (int i = 0; i < studentsMap.size(); i++) {
					studentIds.add(studentsMap.get(i).getStudent().getId());
				}
				response.put("student", studentRepo.getByStudentsByIdsBySchool(studentIds, schoolId));
			} else {
				response.put("message", "No Students found for selected class");
			}
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

	public ResponseEntity<?> getPendingStudentsBySchoolId(long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {

			List<StudentEntity> studentList = studentRepo.getByStudentsPendingBySchool(true, schoolId);
			if (studentList.size() > 0) {
				response.put("indicator", "success");
				response.put("student", studentList);
			} else {
				response.put("indicator", "success");
				message = "No Students Pending";
				response.put("message", message);
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

	public boolean setClassMapPending(long studentId) {

		try {
			Optional<StudentEntity> actStudent = studentRepo.findById(studentId);
			if (actStudent.get().isPending()) {
				actStudent.get().setPending(false);
				studentRepo.saveAndFlush(actStudent.get());
				return true;
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return false;
	}

	public ResponseEntity<?> getStudentsById(long schoolId, long studentId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			StudentEntity studentObj = studentRepo.getByStudentIdBySchool(studentId, schoolId);
			if (studentObj == null) {
				response.put("indicator", "fail");
				response.put("message", "Student id not found");
			} else {
				response.put("indicator", "success");
				response.put("student", studentObj);
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
