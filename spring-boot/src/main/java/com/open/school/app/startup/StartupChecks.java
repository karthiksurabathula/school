package com.open.school.app.startup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.open.school.app.api.entity.CalendarDaysEntity;
import com.open.school.app.api.repository.CalendarDaysRepository;
import com.open.school.app.api.service.InitializationTrackerServiceImpl;
import com.open.school.app.jwt.entity.ApiRequestEntity;
import com.open.school.app.jwt.entity.EntitlementByRoleEntity;
import com.open.school.app.jwt.entity.LoginUser;
import com.open.school.app.jwt.entity.UserRoleEntity;
import com.open.school.app.jwt.repository.ApiRequestRepository;
import com.open.school.app.jwt.repository.EntitlementByRoleRepository;
import com.open.school.app.jwt.repository.LoginUserRepository;
import com.open.school.app.jwt.repository.UserRoleRepository;
import com.open.school.app.jwt.service.JwtService;

@Component
public class StartupChecks {

	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private CalendarDaysRepository calendarDayRepository;
	@Autowired
	private ApiRequestRepository apiRepository;
	@Autowired
	private EntitlementByRoleRepository entitlementByRoleRepository;
	@Autowired
	private JwtService jwsService;
	@Autowired
	private LoginUserRepository loginUser;
	@Autowired
	private InitializationTrackerServiceImpl initiService;

	@EventListener(ApplicationReadyEvent.class)
	void startup() {
		addUserRoles();
		addCalendarDays();
		populateApiRequestsAndPermissions();
		createSuperAdmin();
		initiService.initializeData();

	}

	public void addUserRoles() {
		String roles = "SUPERUSER,ADMIN,TEACHER,STUDENT";
		String[] role = roles.split(",");
		List<UserRoleEntity> userRoles = userRoleRepository.findAll();
		int skip = 0;

		for (int j = 0; j < role.length; j++) {
			skip = 0;
			for (int i = 0; i < userRoles.size(); i++) {
				if (role[j].equals(userRoles.get(i).getRole())) {
					skip = 1;
				}
			}
			if (skip == 0) {
				userRoleRepository.saveAndFlush(new UserRoleEntity(role[j], null));
			}
		}
	}

	public void addCalendarDays() {
		String days = "Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday";
		String[] day = days.split(",");
		List<CalendarDaysEntity> calendarDays = calendarDayRepository.findAll();
		int skip = 0;

		for (int j = 0; j < day.length; j++) {
			skip = 0;
			for (int i = 0; i < calendarDays.size(); i++) {
				if (day[j].equals(calendarDays.get(i).getDay())) {
					skip = 1;
				}
			}
			if (skip == 0) {
				calendarDayRepository.saveAndFlush(new CalendarDaysEntity(day[j], null));
			}
		}
	}

	public void populateApiRequestsAndPermissions() {
		int skip = 0;

		String[] requests = { "schoolCity,GET,SchoolCityController,List all cities;SUPERUSER", "schoolCity,POST,SchoolCityController,Create new city;SUPERUSER", "schoolCity,PUT,SchoolCityController,Update existing city;SUPERUSER", "schoolCity,DELETE,SchoolCityController,Delete existing city;SUPERUSER",

				"school,GET,SchoolController,List all Schools;SUPERUSER", "schoolByUser,GET,SchoolController,Get School assigned to the user;ADMIN", "school,POST,SchoolController,Create new School;SUPERUSER", "school,PUT,SchoolController,Update existing School;SUPERUSER,ADMIN", "school,DELETE,SchoolController,Delete existing School;SUPERUSER",

				"class,GET,ClassController,List of Classes by School;SUPERUSER,ADMIN,TEACHER,STUDENT", "class,POST,ClassController,Create new Class;SUPERUSER,ADMIN", "class,PUT,ClassController,Update existing Class;SUPERUSER,ADMIN", "class,DELETE,ClassController,Delete existing Class;SUPERUSER,ADMIN",

				"section,GET,SectionController,List of Sections by School;SUPERUSER,ADMIN,TEACHER,STUDENT", "section,POST,SectionController,Create new Sections;SUPERUSER,ADMIN", "section,PUT,SectionController,Update existing Sections;SUPERUSER,ADMIN", "section,DELETE,SectionController,Delete existing Sections;SUPERUSER,ADMIN",

				"subject,GET,SubjectController,List of Subjects by School;SUPERUSER,ADMIN,TEACHER,STUDENT", "subject,POST,SubjectController,Create new Subject;SUPERUSER,ADMIN", "subject,PUT,SubjectController,Update existing Subject;SUPERUSER,ADMIN", "subject,DELETE,SubjectController,Delete existing Subject;SUPERUSER,ADMIN",

				"subjectClassMap,GET,SubjectController,List of Subjects by Class;SUPERUSER,ADMIN,TEACHER,STUDENT", "subjectClassMap,POST,SubjectController,Create new Subject Class Map;SUPERUSER,ADMIN", "subjectClassMap,PUT,SubjectController,Update existing Subject Class Map;SUPERUSER,ADMIN", "subjectClassMap,DELETE,SubjectController,Delete existing Subject Class Map;SUPERUSER,ADMIN",

				"staff,GET,StaffController,List of Staff by School;SUPERUSER,ADMIN", "staff,POST,StaffController,Create new Staff;SUPERUSER,ADMIN", "staff,PUT,StaffController,Update existing Staff;SUPERUSER,ADMIN", "staff,DELETE,StaffController,Delete Staff by School;SUPERUSER,ADMIN",

				"studentMap,POST,StudentController,Assign Student to Class and Section;SUPERUSER,ADMIN", "studentMap,GET,StudentController,Get Student to Class and Section;SUPERUSER,ADMIN",

				"student,GET,StudentController,Get Student by Studend ID(String);SUPERUSER,ADMIN", "studentByClass,GET,StudentController,Get Student by ClassId;SUPERUSER,ADMIN", "student,POST,StudentController,Create new Student;SUPERUSER,ADMIN", "student,PUT,StudentController,Create new Student;SUPERUSER,ADMIN", "student,DELETE,StudentController,Delete existing Student;SUPERUSER,ADMIN", "studentById,GET,StudentController,Get student by Id;SUPERUSER,ADMIN", "studentPending,GET,StudentController,Get student not mapped to calss;SUPERUSER,ADMIN",

				"role,GET,UserRolesController,Get all roles;SUPERUSER,ADMIN", "roleByStaff,GET,UserRolesController,Get role by Staff ID;SUPERUSER,ADMIN",

				"subjectStaffMap,POST,SubjectStaffMapController,Assign Staff to Subject;SUPERUSER,ADMIN", "subjectStaffMap,GET,SubjectStaffMapController,Get Staff Mapped to Subject;SUPERUSER,ADMIN",

				"timeTable,POST,TimeTableController,Create Timetable By Class;SUPERUSER,ADMIN", "timeTable,GET,TimeTableController,Get timetable by Class;SUPERUSER,ADMIN,TEACHER,STUDENT", "timeTableStaff,GET,TimeTableController,Get timetable by Staff;TEACHER",

				"period,POST,PeriodController,Create Periods for School;SUPERUSER,ADMIN", "period,GET,PeriodController,Get period by school;SUPERUSER,ADMIN,TEACHER,STUDENT", "period,DELETE,PeriodController,Delete Periods for School;SUPERUSER,ADMIN",

				"exam,GET,ExamController,List exams by School;SUPERUSER,ADMIN,TEACHER", "examByClass,GET,ExamController,List exams by School and Class;SUPERUSER,STUDENT", "examBySchool,GET,ExamController,List exams by School by Scope;SUPERUSER,ADMIN,TEACHER", "exam,POST,ExamController,Create new Exam;SUPERUSER,ADMIN,TEACHER", "exam,PUT,ExamController,Update existing Exam;SUPERUSER,ADMIN,TEACHER", "exam,DELETE,ExamController,Delete existing Exam;SUPERUSER,ADMIN", "examByTeacher,GET,ExamController,Get exams by teacher;SUPERUSER,ADMIN,TEACHER",

				"examTimetable,GET,ExamTimeTableController,Get timetable of exam by class;SUPERUSER,ADMIN,TEACHER,STUDENT", "examTimetableSchool,GET,ExamTimeTableController,Get timetable of exam by School;SUPERUSER,ADMIN", "examTimetable,POST,ExamTimeTableController,Save timetable of exam by class;SUPERUSER,ADMIN,TEACHER",

				"examNotes,GET,ExamNotesController,Get exam Notes;SUPERUSER,ADMIN,TEACHER,STUDENT", "examNotes,POST,ExamNotesController,Create new Exam Notes;SUPERUSER,ADMIN,TEACHER",

				"marksBySubject,GET,MarksController,Get mark by Subject;SUPERUSER,ADMIN,TEACHER", "marksBySubject,POST,MarksController,Create new Marks By Subject;SUPERUSER,ADMIN,TEACHER", "marksByStudent,GET,MarksController,Get marks by Student for particular exam;SUPERUSER,ADMIN,TEACHER,STUDENT", "marksByClass,GET,MarksController,Get marks of Students for particular exam by Class;SUPERUSER,ADMIN,TEACHER,STUDENT",

				"marksStatus,GET,MarksStatusTrackerController,Get mark Completion Status by Subject;SUPERUSER,ADMIN,TEACHER",

				"users,GET,JwtAuthenticationController,Get list of usernames by School;SUPERUSER,ADMIN", "userSettings,POST,JwtAuthenticationController,Update user login settings;SUPERUSER,ADMIN", "resetPassword,POST,JwtAuthenticationController,Reset User Password from users list;SUPERUSER,ADMIN",

				"attendance,GET,AttendanceController,Get Students Attendance by Section;SUPERUSER,ADMIN,TEACHER", "attendance,POST,AttendanceController,Add Attendance by Section;SUPERUSER,ADMIN,TEACHER", "absenteesBySchool,GET,AttendanceController,Get Students Attendance by School;SUPERUSER,ADMIN", "attendanceNote,POST,AttendanceController,Add Attendance Note;SUPERUSER,ADMIN,TEACHER", "workingDay,POST,AttendanceController,Create new working day for school;SUPERUSER,ADMIN", "workingDay,GET,AttendanceController,Check working day;SUPERUSER,ADMIN", "attendanceStatus,GET,AttendanceController,Get Attendence Completion Status;SUPERUSER,ADMIN", "absenteesByStudent,GET,AttendanceController,Get Attendence by Student Id (long);SUPERUSER,ADMIN,TEACHER,STUDENT", "absenteesByStudentId,GET,AttendanceController,Get Attendence by absenteesByStudentId String;SUPERUSER,ADMIN,TEACHER",

				"announcement,POST,AnnouncementController,Create announcement;SUPERUSER,ADMIN", "announcementVisibility,PUT,AnnouncementController,Change announcement Visibility;SUPERUSER,ADMIN", "announcement,DELETE,AnnouncementController,Delete announcement;SUPERUSER,ADMIN",

				"syllabus,POST,AnnouncementController,update syllabus details;SUPERUSER,ADMIN,TEACHER",

				"dummyData,POST,DummyDataController,create Dummy Data;SUPERUSER", "dummyData,PUT,DummyDataController,create Dummy Data for Daily tasks;SUPERUSER", "dummyData,GET,DummyDataController,Check if data is being populated;SUPERUSER,ADMIN,TEACHER,STUDENT",

				"appliance,POST,IotController,create iot device;SUPERUSER,ADMIN", "appliance,GET,IotController,get iot device;SUPERUSER,ADMIN" };

		List<ApiRequestEntity> apiRequests = apiRepository.findAll();

		for (int i = 0; i < requests.length; i++) {
			skip = 0;
			String[] apirequest_role = requests[i].split(";");
			String[] request = apirequest_role[0].split(",");
			String[] role = apirequest_role[1].split(",");

			for (int k = 0; k < apiRequests.size(); k++) {
				if (request[0].equals(apiRequests.get(k).getRequest()) && request[1].equals(apiRequests.get(k).getType()))
					skip = 1;
			}

			if (skip == 0) {
				ApiRequestEntity api = apiRepository.saveAndFlush(new ApiRequestEntity(0, request[0], request[1], request[2], request[3], null));
				for (int j = 0; j < role.length; j++) {
					EntitlementByRoleEntity entilement = entitlementByRoleRepository.findEntielementByApiAndRole(api.getId(), role[j]);
					if (entilement == null) {
						entitlementByRoleRepository.saveAndFlush(new EntitlementByRoleEntity(0, true, new UserRoleEntity(role[j], null), api));
					}
				}
			} else {
				ApiRequestEntity api = apiRepository.findApiRequest(request[0], request[1]);
				for (int j = 0; j < role.length; j++) {
					EntitlementByRoleEntity entilement = entitlementByRoleRepository.findEntielementByApiAndRole(api.getId(), role[j]);
					if (entilement == null) {
						entitlementByRoleRepository.saveAndFlush(new EntitlementByRoleEntity(0, true, new UserRoleEntity(role[j], null), api));
					}
				}
			}

		}
	}

	private void createSuperAdmin() {
		LoginUser loggedInUser = loginUser.findByUsername("su");
		if (loggedInUser == null) {
			jwsService.createUser("su", "123", "SUPERUSER", false, 0);
		}
	}
}
