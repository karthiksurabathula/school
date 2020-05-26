package com.open.school.app.api.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.open.school.app.api.entity.AttendanceEntity;
import com.open.school.app.api.entity.AttendenceTrackerEntity;
import com.open.school.app.api.entity.ClassEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.entity.SectionEntity;
import com.open.school.app.api.entity.StudentEntity;
import com.open.school.app.api.entity.WorkingDayEntity;
import com.open.school.app.api.model.attendance.AttendanceReqModel;
import com.open.school.app.api.model.attendance.WorkingDayModel;
import com.open.school.app.api.repository.AttendanceRepository;
import com.open.school.app.api.repository.AttendenceTrackerRepository;
import com.open.school.app.api.repository.StudentMapRepository;
import com.open.school.app.api.repository.StudentRepository;
import com.open.school.app.api.repository.WorkingDayRepository;

@Service
public class AttendanceServiceImpl {

	@Autowired
	private AttendanceRepository attendanceRepo;
	@Autowired
	private StudentMapRepository studentMapRepo;
	@Autowired
	private WorkingDayRepository dayRepo;
	@Autowired
	private AttendenceTrackerRepository attendenceTrackerRepo;
	@Autowired
	private AttendanceTrackerServiceImpl attendanceTrackerService;
	@Autowired
	private StudentRepository studentRepo;

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private static final Logger log = LogManager.getLogger(AttendanceServiceImpl.class);


	public ResponseEntity<?> getAttendanceBySchoolClassSection(long schoolId, long classId, long sectionId) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> attendanceResp = new ArrayList<Object>();

		String strDate = dateFormat.format(new Date());
		int check;
		String message = null;
		List<AttendanceEntity> attendance = null;
		try {
			WorkingDayEntity day = dayRepo.getWorkingDayByDate(schoolId, dateFormat.parse(strDate));
			if (day != null) {
				attendance = attendanceRepo.getAttendanceByClassAndSection(schoolId, classId, sectionId, day.getId());
			}
			if (day != null && attendance.size() > 0) {
				List<Object[]> stuDet = studentMapRepo.getByStudentsDetailsByClass(schoolId, classId, sectionId);
				for (Object[] result : stuDet) {
					check = 0;
					for (int i = 0; i < attendance.size(); i++) {
						StudentEntity stu = attendance.get(i).getStudent();
						if ((long) result[0] == stu.getId()) {
							AttendanceReqModel attendanceReqTemp = new AttendanceReqModel();
							attendanceReqTemp.setStudent(new StudentEntity(stu.getId(), stu.getStudentId(), stu.getName(), null, null, null, null, null, null, null, null, null, null, null, false, false, null, null, null, null, null));
							attendanceReqTemp.setAttendance(attendance.get(i));
							attendanceReqTemp.setDay(new WorkingDayModel(0, strDate));
							attendanceResp.add(attendanceReqTemp);
							check = 1;
							break;
						}
					}
					if (check == 0) {
						AttendanceReqModel attendanceReqTemp = new AttendanceReqModel();
						attendanceReqTemp.setStudent(new StudentEntity((long) result[0], (String) result[1], (String) result[2], null, null, null, null, null, null, null, null, null, null, null, false, false, null, null, null, null, null));
						attendanceReqTemp.setAttendance(new AttendanceEntity(0, false, null, null, null, null, null, null, null, day));
						attendanceReqTemp.setDay(new WorkingDayModel(0, strDate));
						attendanceResp.add(attendanceReqTemp);
					}
				}
			} else {
				List<Object[]> stuDet = studentMapRepo.getByStudentsDetailsByClass(schoolId, classId, sectionId);
				for (Object[] result : stuDet) {
					AttendanceReqModel attendanceReqTemp = new AttendanceReqModel();
					attendanceReqTemp.setStudent(new StudentEntity((long) result[0], (String) result[1], (String) result[2], null, null, null, null, null, null, null, null, null, null, null, false, false, null, null, null, null, null));
					attendanceReqTemp.setAttendance(new AttendanceEntity(0, false, null, null, null, null, null, null, null, day));
					attendanceReqTemp.setDay(new WorkingDayModel(0, strDate));
					attendanceResp.add(attendanceReqTemp);
				}
			}
			response.put("data", attendanceResp);
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

	public ResponseEntity<?> createAttendanceBySchoolClassSection(long schoolId, long classId, long sectionId, List<AttendanceReqModel> attendance) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Date date = dateFormat.parse(attendance.get(0).getDay().getDate());
			WorkingDayEntity day = dayRepo.getWorkingDayByDate(schoolId, date);
			if (day == null) {
				day = dayRepo.saveAndFlush(new WorkingDayEntity(0, date, message, date, new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null), null));
				attendanceTrackerService.createEntriesForAttendenceTracker(day.getId(), schoolId);
			}
			for (int i = 0; i < attendance.size(); i++) {
				AttendanceEntity student = attendanceRepo.getAttendanceByClassAndSectionAndStudent(schoolId, classId, sectionId, day.getId(), attendance.get(i).getStudent().getId());
				if (attendance.get(i).getAttendance().isAbsent() && student == null) {
					AttendanceEntity attendanceNew = new AttendanceEntity();
					attendanceNew.setAbsent(true);
					attendanceNew.setNote(null);
					attendanceNew.setLastModified(new Date());
					attendanceNew.setModifiedBy(auth.getName());
					attendanceNew.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
					attendanceNew.setClasses(new ClassEntity(classId, null, null, null, null, true, null, null));
					attendanceNew.setSection(new SectionEntity(sectionId, null, true, null, null, null, null, null, null));
					attendanceNew.setStudent(new StudentEntity(attendance.get(i).getStudent().getId(), null, null, null, null, null, null, null, null, null, null, null, null, null, true, true, null, null, null, null, null));
					attendanceNew.setDay(day);
					attendanceRepo.saveAndFlush(attendanceNew);
				} else if (!attendance.get(i).getAttendance().isAbsent() && student != null) {
					attendanceRepo.delete(student);
				}
			}
			AttendenceTrackerEntity tracker = attendenceTrackerRepo.getAttendanceTrackerByClassAndSection(schoolId, classId, sectionId, day.getId());
			if (!tracker.isCompleted()) {
				tracker.setCompleted(true);
				attendenceTrackerRepo.saveAndFlush(tracker);
			}
			response.put("indicator", "success");
			response.put("message", "Attence Updated successfully");
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

	public ResponseEntity<?> getAbsenteesBySchool(long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> attendanceResp = new ArrayList<Object>();
		String strDate = dateFormat.format(new Date());
		String message = null;
		List<AttendanceEntity> attendance = null;
		try {
			WorkingDayEntity day = dayRepo.getWorkingDayByDate(schoolId, dateFormat.parse(strDate));
			if (day != null) {
				attendance = attendanceRepo.getAbsenteesBySchool(schoolId, day.getId());
			}
			if (day != null && attendance.size() > 0) {
				for (int i = 0; i < attendance.size(); i++) {
					StudentEntity stu = attendance.get(i).getStudent();
					AttendanceReqModel attendanceReqTemp = new AttendanceReqModel();
					attendanceReqTemp.setStudent(new StudentEntity(stu.getId(), stu.getStudentId(), stu.getName(), null, null, null, null, null, null, null, null, null, null, null, false, false, null, null, null, null, null));
					attendanceReqTemp.setAttendance(attendance.get(i));
					attendanceReqTemp.setDay(new WorkingDayModel(0, strDate));
					attendanceResp.add(attendanceReqTemp);
				}
				response.put("data", attendanceResp);
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

	public ResponseEntity<?> saveAbsenteesNote(long schoolId, AttendanceEntity attendance) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {

			Optional<AttendanceEntity> attendenceObj = attendanceRepo.findById(attendance.getId());
			if (attendenceObj.get() != null) {
				attendenceObj.get().setNote(attendance.getNote());
				attendanceRepo.saveAndFlush(attendenceObj.get());
				response.put("indicator", "success");
				response.put("message", "Note added successfully");
			} else {
				response.put("indicator", "fail");
				response.put("message", "Attendance record not found");
			}

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (

		Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1001");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> initializeWorkingday(long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String strDate = dateFormat.format(new Date());
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			WorkingDayEntity day = dayRepo.getWorkingDayByDate(schoolId, dateFormat.parse(strDate));
			if (day == null) {
				day = dayRepo.saveAndFlush(new WorkingDayEntity(0, dateFormat.parse(dateFormat.format(new Date())), auth.getName(), new Date(), new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null), null));
				attendanceTrackerService.createEntriesForAttendenceTracker(day.getId(), schoolId);
			}
			response.put("day", day);
			response.put("indicator", "success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1001");
			response.put("message", "Error Occured, if issue persists please contact administrator");
		}
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> initializeWorkingdayCheck(long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String strDate = dateFormat.format(new Date());
		try {
			WorkingDayEntity day = dayRepo.getWorkingDayByDate(schoolId, dateFormat.parse(strDate));
			if (day == null) {
				response.put("day", new WorkingDayModel(0, "Not Initialized"));
				response.put("indicator", "fail");
			} else {
				response.put("day", day);
				response.put("indicator", "success");
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1001");
			response.put("message", "Error Occured, if issue persists please contact administrator");
		}
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> getAbsenteesBySchoolAndStudentId(long schoolId, String studentId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			StudentEntity student = studentRepo.getByStudentIdBySchool(studentId, schoolId);
			if (student != null) {
				return getAbsenteesBySchoolAndStudent(schoolId, student.getId());
			} else {
				response.put("indicator", "fail");
				response.put("message", "Student Id Not found");
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

	public ResponseEntity<?> getAbsenteesBySchoolAndStudent(long schoolId, long studentId) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> attendanceResp = new ArrayList<Object>();
		String message = null;
		try {
			List<AttendanceEntity> attendance = attendanceRepo.getAttendanceByClassAndSectionAndStudent(schoolId, studentId);
			if (attendance.size() > 0) {
				StudentEntity student = attendance.get(0).getStudent();
				ClassEntity classs = attendance.get(0).getClasses();
				SectionEntity section = attendance.get(0).getSection();
				for (int i = 0; i < attendance.size(); i++) {
					AttendanceReqModel attendanceReqTemp = new AttendanceReqModel();
					WorkingDayEntity day = attendance.get(i).getDay();
					attendanceReqTemp.setAttendance(attendance.get(i));
					attendanceReqTemp.setDay(new WorkingDayModel(day.getId(), dateFormat.format(day.getDate())));
					attendanceReqTemp.setStudent(student);
					attendanceReqTemp.setClasses(classs);
					attendanceReqTemp.setSection(section);
					attendanceResp.add(attendanceReqTemp);
				}

			} else {
				response.put("message", "No records found");
			}
			response.put("data", attendanceResp);
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
