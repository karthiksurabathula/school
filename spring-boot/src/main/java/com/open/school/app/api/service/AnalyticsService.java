package com.open.school.app.api.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import com.open.school.app.api.entity.StaffEntity;
import com.open.school.app.api.entity.StudentEntity;
import com.open.school.app.api.entity.StudentMapEntity;
import com.open.school.app.api.entity.WorkingDayEntity;
import com.open.school.app.api.model.analytics.AnalyticsModel;
import com.open.school.app.api.repository.AnnouncementRepository;
import com.open.school.app.api.repository.AttendanceRepository;
import com.open.school.app.api.repository.ExamRepository;
import com.open.school.app.api.repository.SchoolCityRepository;
import com.open.school.app.api.repository.SchoolRepository;
import com.open.school.app.api.repository.StaffRepository;
import com.open.school.app.api.repository.StudentMapRepository;
import com.open.school.app.api.repository.StudentRepository;
import com.open.school.app.api.repository.WorkingDayRepository;
import com.open.school.app.jwt.entity.LoginUser;
import com.open.school.app.jwt.repository.LoginUserRepository;

@Service
public class AnalyticsService {

	@Autowired
	private LoginUserRepository loginUser;
	@Autowired
	private StudentRepository studentRepo;
	@Autowired
	private SchoolCityRepository cityRepo;
	@Autowired
	private SchoolRepository schoolRepo;
	@Autowired
	private StaffRepository staffRepo;
	@Autowired
	private ExamRepository examRepo;
	@Autowired
	private AttendanceRepository attendanceRepo;
	@Autowired
	private WorkingDayRepository dayRepo;
	@Autowired
	private AnnouncementRepository announcementRepo;
	@Autowired
	private StudentMapRepository studentMapRepository;

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private static final Logger log = LogManager.getLogger(AnalyticsService.class);

	public ResponseEntity<?> analytics(long schoolId) {

		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			List<Object> analytics = new ArrayList<Object>();
			final LoginUser user = loginUser.findByUsername(auth.getName());
			if (user.getRole().getRole().equals("SUPERUSER")) {
				// Get no of cities, schools, students, users
				analytics.add(new AnalyticsModel("Cities", cityRepo.count(), "location.png", "/city"));
				analytics.add(new AnalyticsModel("Schools", schoolRepo.count(), "school.png", "/school"));
				analytics.add(new AnalyticsModel("Students", studentRepo.count(), "student.png", "/student"));
				analytics.add(new AnalyticsModel("Users", loginUser.count(), "teacher.png", "/staff"));
			} else {

				if (user.getRole().getRole().equals("ADMIN")) {

					WorkingDayEntity day = dayRepo.getWorkingDayByDate(schoolId, dateFormat.parse(dateFormat.format(new Date())));

					analytics.add(new AnalyticsModel("Students", studentRepo.getByStudentsCountBySchool(schoolId), "student.png", "/student"));
					analytics.add(new AnalyticsModel("Staff", staffRepo.getByStaffCountBySchoolAndId(schoolId), "teacher.png", "/staff"));

					if (day == null) {
						analytics.add(new AnalyticsModel("Absentees", 0, "sick.png", "/attendance-tracker"));
					} else {
						analytics.add(new AnalyticsModel("Absentees", attendanceRepo.getAbsenteesBySchoolCount(schoolId, day.getId()), "sick.png", "/attendance-tracker"));
					}

					analytics.add(new AnalyticsModel("Upcoming Exams", examRepo.getExamsByCompletionStatusAndSchool(false, schoolId), "test.png", "/exam"));
					analytics.add(new AnalyticsModel("Announcements", announcementRepo.getCurrentAnnouncementCount(schoolId), "bell.png", "/announcement"));
					analytics.add(new AnalyticsModel("Timetable", 0, "clock.png", "/timetable"));
				} else if (user.getRole().getRole().equals("TEACHER")) {

					StaffEntity staff = staffRepo.getByStaffBySchoolAndIdString(schoolId, auth.getName());
					System.out.println(schoolId + " " + staff.getId());
					analytics.add(new AnalyticsModel("Attendance", 0, "attendance.png", "/attendance"));
					analytics.add(new AnalyticsModel("Upcoming Exams", examRepo.getUpcommingtExamsStaff(schoolId, false, staff.getId()), "test.png", "/exam"));
					analytics.add(new AnalyticsModel("Timetable", 0, "clock.png", "/period-staff"));
					analytics.add(new AnalyticsModel("Announcements", announcementRepo.getCurrentAnnouncementCount(schoolId), "bell.png", "/announcement"));
				} else if (user.getRole().getRole().equals("STUDENT")) {

					StudentEntity student = studentRepo.getByStudentsByUsername(auth.getName());
					StudentMapEntity studentMap = studentMapRepository.getByStuId(student.getId());
					long examCount = examRepo.getExamsCountBySchoolAndClassAndSection(schoolId, studentMap.getClasses().getId(), studentMap.getSection().getId(), false);

					analytics.add(new AnalyticsModel("Upcoming Exams", examCount, "test.png", "/exam"));
					analytics.add(new AnalyticsModel("Timetable", 0, "clock.png", "/timetable"));
					analytics.add(new AnalyticsModel("Announcements", announcementRepo.getCurrentAnnouncementCount(schoolId), "bell.png", "/announcement"));
				}
			}
			response.put("analytics", analytics);
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
