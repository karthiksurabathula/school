package com.open.school.app.dummyData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.AnnouncementEntity;
import com.open.school.app.api.entity.AttendanceEntity;
import com.open.school.app.api.entity.AttendenceTrackerEntity;
import com.open.school.app.api.entity.CalendarDaysEntity;
import com.open.school.app.api.entity.ClassEntity;
import com.open.school.app.api.entity.ExamEntity;
import com.open.school.app.api.entity.ExamNotesEntity;
import com.open.school.app.api.entity.ExamTimeTableEntity;
import com.open.school.app.api.entity.InitializationTrackerEntity;
import com.open.school.app.api.entity.MarksEntity;
import com.open.school.app.api.entity.MarksStatusTrackerEntity;
import com.open.school.app.api.entity.MarksTrackerEntity;
import com.open.school.app.api.entity.PeriodEntity;
import com.open.school.app.api.entity.SchoolCityEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.entity.SectionEntity;
import com.open.school.app.api.entity.StaffEntity;
import com.open.school.app.api.entity.StudentEntity;
import com.open.school.app.api.entity.StudentMapEntity;
import com.open.school.app.api.entity.SubjectClassMapEntity;
import com.open.school.app.api.entity.SubjectEntity;
import com.open.school.app.api.entity.SubjectStaffMapEntity;
import com.open.school.app.api.entity.SyllabusEntity;
import com.open.school.app.api.entity.TimeTableEntity;
import com.open.school.app.api.entity.WorkingDayEntity;
import com.open.school.app.api.repository.AnnouncementRepository;
import com.open.school.app.api.repository.AttendanceRepository;
import com.open.school.app.api.repository.AttendenceTrackerRepository;
import com.open.school.app.api.repository.CalendarDaysRepository;
import com.open.school.app.api.repository.ClassRepository;
import com.open.school.app.api.repository.ExamNotesRepository;
import com.open.school.app.api.repository.ExamRepository;
import com.open.school.app.api.repository.ExamTimeTableRepository;
import com.open.school.app.api.repository.InitializationTrackerRepository;
import com.open.school.app.api.repository.MarksRepository;
import com.open.school.app.api.repository.MarksStatusTrackerRepository;
import com.open.school.app.api.repository.MarksTrackerRepository;
import com.open.school.app.api.repository.PeriodRepository;
import com.open.school.app.api.repository.SchoolCityRepository;
import com.open.school.app.api.repository.SchoolRepository;
import com.open.school.app.api.repository.SectionRepository;
import com.open.school.app.api.repository.StaffRepository;
import com.open.school.app.api.repository.StudentMapRepository;
import com.open.school.app.api.repository.StudentRepository;
import com.open.school.app.api.repository.SubjectClassMapRepository;
import com.open.school.app.api.repository.SubjectRepository;
import com.open.school.app.api.repository.SubjectStaffMapRepository;
import com.open.school.app.api.repository.SyllabusRepository;
import com.open.school.app.api.repository.TimeTableRepository;
import com.open.school.app.api.repository.WorkingDayRepository;
import com.open.school.app.api.service.AttendanceTrackerServiceImpl;
import com.open.school.app.api.service.ExamServiceImpl;
import com.open.school.app.api.service.InitializationTrackerServiceImpl;
import com.open.school.app.jwt.entity.UserRoleEntity;
import com.open.school.app.jwt.service.JwtService;

@Service
public class DummyDataLogicService {

	@Autowired
	private JwtService jwsService;
	@Autowired
	private SchoolCityRepository schoolCityRepo;
	@Autowired
	private SchoolRepository schoolRepo;
	@Autowired
	private ClassRepository classrepo;
	@Autowired
	private SectionRepository sectionRepo;
	@Autowired
	private SubjectRepository subjectRepo;
	@Autowired
	private StaffRepository staffRepo;
	@Autowired
	private StudentRepository studentRepo;
	@Autowired
	private StudentMapRepository studentMapRepository;
	@Autowired
	private PeriodRepository periodRepository;
	@Autowired
	private SubjectClassMapRepository subjectClassMapRepository;
	@Autowired
	private SubjectStaffMapRepository subjectStaffMapRepository;
	@Autowired
	private CalendarDaysRepository calendarDayRepository;
	@Autowired
	private TimeTableRepository timeTableRepository;
	@Autowired
	private ExamRepository examRepo;
	@Autowired
	private ExamTimeTableRepository examTimeTableRepo;
	@Autowired
	private ExamNotesRepository examNoteRepo;
	@Autowired
	private MarksTrackerRepository marksTrackerRepo;
	@Autowired
	private MarksRepository marksRepo;
	@Autowired
	private MarksStatusTrackerRepository marksStatusRepo;
	@Autowired
	private InitializationTrackerServiceImpl intiService;
	@Autowired
	private InitializationTrackerRepository initiRepo;
	@Autowired
	private WorkingDayRepository dayRepo;
	@Autowired
	private AttendenceTrackerRepository attendenceTrackerRepo;
	@Autowired
	private AttendanceTrackerServiceImpl attendanceTrackerService;
	@Autowired
	private AttendanceRepository attendanceRepo;
	@Autowired
	private SyllabusRepository syllabusRepo;
	@Autowired
	private AnnouncementRepository announcementRepo;
	@Autowired
	private ExamServiceImpl examService;

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

//	@EventListener(ApplicationReadyEvent.class)
	@Async("asyncExecutor")
	public void loadData(long cityCount, long schoolCount, long classCount, long sectionCount, long subjectCount, long staffCount, long studentCout, long periodCount) {

		InitializationTrackerEntity initTracker;

//		int cityCount = 1, schoolCount = 1, classCount = 2, sectionCount = 2, subjectCount = 8, staffCount = 10, studentCout = 30, periodCount = 8;

		// Create Students
		long totalStudents = studentCout * classCount * sectionCount;

		// City
		for (int i = 0; i < cityCount; i++) {
			long cityId = createCity(i + 1 + schoolCityRepo.count()).getId();

			// School
			for (int j = 0; j < schoolCount; j++) {

				long sclId = createSchool(cityId, j + 1 + schoolRepo.count()).getId();

				initTracker = initiRepo.getMarksBySclStuExamSubj("datacreation");
				initTracker.setStatus(true);
				initTracker.setLastModified(new Date());
				initTracker.setModifiedBy("su");
				initiRepo.saveAndFlush(initTracker);

				// Class
				for (int a = 0; a < classCount; a++) {
					long classId = createClass(cityId, sclId, a + 1).getId();

					// Section
					for (int b = 0; b < sectionCount; b++) {
						createSection(sclId, classId, b + 1).getId();
					}
				}

				// Subject
				for (int a = 0; a < subjectCount; a++) {
					createSubject(sclId, a + 1);
				}

				// Subject Class Map
				subjectClassMap(sclId, subjectCount);

				// Create Students
				for (int a = 0; a < totalStudents; a++) {
					createStudent(sclId);
				}

				// Student Class Map
				studenClassMap(sclId, studentCout);

				// Staff
				for (int a = 0; a < staffCount; a++) {
					createStaff(sclId);
				}

				// Subject Staff Map
				subjectStaffMap(sclId);

				// Period
				if (periodCount > 8) {
					periodCount = 8;
				}
				for (int a = 0; a < periodCount; a++) {
					createPeriod(sclId, a + 1);
				}

				// Period Time Table
				createPeriodTimeTable(sclId);

				// Create Exam
				createExam(sclId);

				// Create exam timetable
				createExamTimeTable(sclId);

				// Add Marks By Subject for all School level exams
				addMarksBySubject(sclId);

			}
		}

		initTracker = initiRepo.getMarksBySclStuExamSubj("datacreation");
		initTracker.setStatus(false);
		initTracker.setLastModified(new Date());
		initTracker.setModifiedBy("su");
		initiRepo.saveAndFlush(initTracker);

		System.out.println("Data loadded successfully");
	}

	private SchoolCityEntity createCity(long cityCount) {
		SchoolCityEntity sclCity = new SchoolCityEntity();
		sclCity.setCity("City" + cityCount);
		sclCity.setState("state" + cityCount);
		sclCity.setStatus(true);
		sclCity.setLastModified(new Date());
		sclCity.setCreatedDate(new Date());
		sclCity.setModifiedBy("dummy-dataloader");
		return schoolCityRepo.saveAndFlush(sclCity);
	}

	private SchoolEntity createSchool(long cityId, long count) {
		SchoolEntity school = new SchoolEntity();
		// schoolCode
		school.setSchoolCode(RandomStringUtils.randomAlphabetic(4).toUpperCase() + count);
		school.setSchoolName("SCL" + count);
		school.setSchoolPhone("99999999999");
		school.setSchoolEmail("scl" + count + "@fakemail.com");
		school.setLocation("Location" + count);
		school.setAddress("Add" + count);
		school.setStatus(true);
		school.setLastModified(new Date());
		school.setCreatedDate(new Date());
		school.setModifiedBy("dummy-dataloader");
		school.setSchoolCity(new SchoolCityEntity(cityId, null, null, true, null, null, null, null));
		SchoolEntity schoolResp = schoolRepo.saveAndFlush(school);
		jwsService.createAdminUser("123", false, schoolResp.getId());
		intiService.initializeClass(schoolResp.getId(), true);
		return schoolResp;
	}

	private ClassEntity createClass(long cityId, long sclId, long count) {
		ClassEntity classs = new ClassEntity();
		classs.setClassName("Class" + count);
		classs.setStatus(true);
		classs.setLastModified(new Date());
		classs.setCreatedDate(new Date());
		classs.setModifiedBy("dummy-dataloader");
		classs.setSchool(new SchoolEntity(sclId, null, null, null, null, null, null, true, null, null, null, null, null, null));
		return classrepo.saveAndFlush(classs);
	}

	private SectionEntity createSection(long sclId, long classId, long count) {
		SectionEntity section = new SectionEntity();
		section.setSectionName("SEC" + count);
		section.setStatus(true);
		section.setLastModified(new Date());
		section.setCreatedDate(new Date());
		section.setModifiedBy("dummy-dataloader");
		section.setSchool(new SchoolEntity(sclId, null, null, null, null, null, null, true, null, null, null, null, null, null));
		section.setClasses(new ClassEntity(classId, null, null, null, null, true, null, null));
		return sectionRepo.saveAndFlush(section);
	}

	private SubjectEntity createSubject(long sclId, long count) {
		SubjectEntity subject = new SubjectEntity();
		subject.setSubjectName("SUB" + count);
		subject.setLastModified(new Date());
		subject.setCreatedDate(new Date());
		subject.setModifiedBy("dummy-dataloader");
		subject.setSchool(new SchoolEntity(sclId, null, null, null, null, null, null, true, null, null, null, null, null, null));
		return subjectRepo.saveAndFlush(subject);

	}

	private StaffEntity createStaff(long sclId) {
		long count = (staffRepo.getByStaffCountBySchoolAndId(sclId) + 1);
		String staffId = "STF" + ((100000 * sclId) + count);
		StaffEntity staff = new StaffEntity();
		if (count % 2 == 1) {
			staff.setGender("female");
		} else {
			staff.setGender("male");
		}
		staff.setName(staffId);
		staff.setStatus(true);
		staff.setLastModified(new Date());
		staff.setCreatedDate(new Date());
		staff.setModifiedBy("dummy-dataloader");
		staff.setStaffId(staffId);
		staff.setSchool(new SchoolEntity(sclId, null, null, null, null, null, null, true, null, null, null, null, null, null));
		staff.setRole(new UserRoleEntity("TEACHER", null));
		StaffEntity staffObj = staffRepo.saveAndFlush(staff);
		jwsService.createUser(staffId, "1234", "TEACHER", false, sclId);
		return staffObj;
	}

	private StudentEntity createStudent(long sclId) {
		StudentEntity student = new StudentEntity();
		long count = (studentRepo.getCountOfStudentsBySchool(sclId) + 1);
		String userId = "STU" + ((100000 * sclId) + (studentRepo.getCountOfStudentsBySchool(sclId) + 1));
		student.setStudentId(userId);
		if (count % 2 == 1) {
			student.setGender("female");
		} else {
			student.setGender("male");
		}
		student.setUsername(userId);
		student.setName(userId);
		student.setDob("13-5-1995");
		student.setFatherName("FA" + count);
		student.setMotherName("MO" + count);
		student.setStatus(true);
		student.setPending(true);
		student.setLastModified(new Date());
		student.setCreatedDate(new Date());
		student.setModifiedBy("dummy-dataloader");
		student.setSchool(new SchoolEntity(sclId, null, null, null, null, null, null, true, null, null, null, null, null, null));
		StudentEntity stu = studentRepo.saveAndFlush(student);
		jwsService.createUser(userId, "1234", "STUDENT", false, sclId);
		return stu;
	}

	private PeriodEntity createPeriod(long sclId, long count) {
		PeriodEntity period = new PeriodEntity();
		period.setDescription("Period" + count);
		period.setStartTime(DateUtils.addHours(new Date(System.currentTimeMillis()), +(1 + (int) count)));
		period.setEndTime(DateUtils.addHours(new Date(System.currentTimeMillis()), +(2 + (int) count)));
		period.setLastModified(new Date());
		period.setModifiedBy("dummy-dataloader");
		period.setSchool(new SchoolEntity(sclId, null, null, null, null, null, null, true, null, null, null, null, null, null));
		return periodRepository.saveAndFlush(period);
	}

	private void studenClassMap(long sclId, long count) {
		List<ClassEntity> classes = classrepo.getClassessBySchool(sclId);
		List<StudentEntity> students = studentRepo.getByStudentsBySchool(sclId);
		long studentCount = count - 3;
		int stuCount = 0;
		for (int i = 0; i < classes.size(); i++) {
			List<SectionEntity> section = sectionRepo.getBySectionsBySchoolAndClass(sclId, classes.get(i).getId());
			for (int j = 0; j < section.size(); j++) {
				for (int k = 0; k < studentCount; k++) {
					// add map
					StudentMapEntity studentMap = new StudentMapEntity();
					studentMap.setSchool(new SchoolEntity(sclId, null, null, null, null, null, null, true, null, null, null, null, null, null));
					studentMap.setClasses(new ClassEntity(classes.get(i).getId(), null, null, null, null, true, null, null));
					studentMap.setSection(new SectionEntity(section.get(j).getId(), null, true, null, null, null, null, null, null));
					studentMap.setStudent(new StudentEntity(students.get(stuCount).getId(), null, null, null, null, null, null, null, null, null, null, null, null, null, true, true, null, null, null, null, null));
					studentMap.setModifiedBy("dummy-dataloader");
					studentMap.setLastModified(new Date());
					studentMap.setCreatedDate(new Date());
					studentMapRepository.saveAndFlush(studentMap);

					// Clear pending
					students.get(stuCount).setPending(false);
					studentRepo.saveAndFlush(students.get(stuCount));

					stuCount = stuCount + 1;
				}
			}
		}
	}

	private void subjectClassMap(long sclId, long subjCount) {
		List<SubjectEntity> subjects = subjectRepo.getSubjectsBySchool(sclId);
		List<ClassEntity> classes = classrepo.getClassessBySchool(sclId);

		for (int i = 0; i < classes.size(); i++) {

			for (int j = 0; j < subjCount - 2; j++) {
				SubjectClassMapEntity subjectMapNew = new SubjectClassMapEntity();
				subjectMapNew.setSchool(new SchoolEntity(sclId, null, null, null, null, null, null, true, null, null, null, null, null, null));
				subjectMapNew.setClasses(new ClassEntity(classes.get(i).getId(), null, null, null, null, true, null, null));
				subjectMapNew.setSubject(new SubjectEntity(subjects.get(j).getId(), null, null, null, null, null, null));
				subjectMapNew.setModifiedBy("dummy-dataloader");
				if (j + 1 == subjCount - 2 || j + 2 == subjCount - 2) {
					subjectMapNew.setOptional(true);
				} else {
					subjectMapNew.setOptional(false);
				}
				subjectMapNew.setLastModified(new Date());
				subjectMapNew.setCreatedDate(new Date());
				subjectClassMapRepository.saveAndFlush(subjectMapNew);
			}
		}
	}

	private void subjectStaffMap(long sclId) {

		List<ClassEntity> classes = classrepo.getClassessBySchool(sclId);
		List<StaffEntity> staff = staffRepo.getByStaffBySchool(sclId);
		int tstaffCount = 0;
		for (int i = 0; i < classes.size(); i++) {
			List<Object[]> subjectsMap = subjectClassMapRepository.getSubjectsMappedToSchoolClass(sclId, classes.get(i).getId());
			for (Object[] result : subjectsMap) {

				List<SectionEntity> section = sectionRepo.getBySectionsBySchoolAndClass(sclId, classes.get(i).getId());
				for (int j = 0; j < section.size(); j++) {
					if (tstaffCount == subjectsMap.size()) {
						tstaffCount = 0;
					}
					SubjectStaffMapEntity subjectStaffMap = new SubjectStaffMapEntity();
					subjectStaffMap.setSchool(new SchoolEntity(sclId, null, null, null, null, null, null, true, null, null, null, null, null, null));
					subjectStaffMap.setClasses(new ClassEntity(classes.get(i).getId(), null, null, null, null, true, null, null));
					subjectStaffMap.setSection(new SectionEntity(section.get(j).getId(), null, true, null, null, null, null, null, null));
					subjectStaffMap.setSubject(new SubjectEntity((long) result[3], null, null, null, null, null, null));
					subjectStaffMap.setStaff(new StaffEntity(staff.get(tstaffCount).getId(), null, null, null, null, null, null, null, null, null, null, null, true, null, null, null, null));
					subjectStaffMap.setModifiedBy("dummy-dataloader");
					subjectStaffMap.setLastModified(new Date());
					subjectStaffMap.setCreatedDate(new Date());
					subjectStaffMapRepository.saveAndFlush(subjectStaffMap);
					tstaffCount = tstaffCount + 1;
				}
			}
		}
	}

	private void createPeriodTimeTable(long sclId) {
		List<PeriodEntity> periods = periodRepository.getPeriodsBySchool(sclId);
		List<CalendarDaysEntity> days = calendarDayRepository.findAll();
		List<ClassEntity> classes = classrepo.getClassessBySchool(sclId);
		for (int i = 0; i < classes.size(); i++) {
			List<SubjectClassMapEntity> subjects = subjectClassMapRepository.getSubjectClassMapByClass(sclId, classes.get(i).getId());
			int subjcount = 0;
			List<SectionEntity> sections = sectionRepo.getBySectionsBySchoolAndClass(sclId, classes.get(i).getId());
			for (int j = 0; j < sections.size(); j++) {
				for (int k = 0; k < days.size(); k++) {
					for (int l = 0; l < periods.size(); l++) {
						if (subjcount == subjects.size()) {
							subjcount = 0;
						}
						TimeTableEntity timeTableNew = new TimeTableEntity();
						timeTableNew.setModifiedBy("dummy-dataloader");
						timeTableNew.setCreatedDate(new Date());
						timeTableNew.setMaks(100);
						timeTableNew.setLastModified(new Date());
						timeTableNew.setSchool(new SchoolEntity(sclId, null, null, null, null, null, null, true, null, null, null, null, null, null));
						timeTableNew.setClasses(new ClassEntity(classes.get(i).getId(), null, null, null, null, true, null, null));
						timeTableNew.setSection(new SectionEntity(sections.get(j).getId(), null, true, null, null, null, null, null, null));
						timeTableNew.setSubject(new SubjectEntity(subjects.get(subjcount).getSubject().getId(), null, null, null, null, null, null));
						timeTableNew.setCalendarDay(new CalendarDaysEntity(days.get(k).getDay(), null));
						timeTableNew.setPeriod(new PeriodEntity(periods.get(l).getId(), null, 0, 0, null, null, null, null));
						timeTableRepository.saveAndFlush(timeTableNew);
						subjcount = subjcount + 1;
					}
				}

			}
		}
	}

	private void createExam(long sclId) {

		int count = examRepo.findAll().size() + 1;
		ExamEntity exam = new ExamEntity();

		exam.setName("Exam" + count);
		exam.setDescription("Exam" + count + " Desec");
		exam.setScope("School");
		exam.setLastModified(new Date());
		exam.setCreatedDate(new Date());
		exam.setCompleted(false);
		exam.setModifiedBy("dummy-dataloader");
		exam.setSchool(new SchoolEntity(sclId, null, null, null, null, null, null, true, null, null, null, null, null, null));
		ExamEntity examResp = examRepo.saveAndFlush(exam);
		addMarksStatus(sclId, examResp.getId());
		count = count + 1;

		List<ClassEntity> classes = classrepo.getClassessBySchool(sclId);
		for (int i = 0; i < classes.size(); i++) {
			exam = new ExamEntity();

			exam.setName("Exam" + count);
			exam.setDescription("Exam" + count + " Desec");
			exam.setScope("Class");
			exam.setLastModified(new Date());
			exam.setCreatedDate(new Date());
			exam.setCompleted(false);
			exam.setModifiedBy("dummy-dataloader");
			exam.setSchool(new SchoolEntity(sclId, null, null, null, null, null, null, true, null, null, null, null, null, null));
			exam.setClasses(new ClassEntity(classes.get(i).getId(), null, null, null, null, true, null, null));
			examRepo.saveAndFlush(exam);
			count = count + 1;

			List<SectionEntity> sections = sectionRepo.getBySectionsBySchoolAndClass(sclId, classes.get(i).getId());
			for (int j = 0; j < sections.size(); j++) {
				exam = new ExamEntity();

				exam.setName("Exam" + count);
				exam.setDescription("Exam" + count + " Desec");
				exam.setScope("Section");
				exam.setLastModified(new Date());
				exam.setCreatedDate(new Date());
				exam.setCompleted(false);
				exam.setModifiedBy("dummy-dataloader");
				exam.setSchool(new SchoolEntity(sclId, null, null, null, null, null, null, true, null, null, null, null, null, null));
				exam.setClasses(new ClassEntity(classes.get(i).getId(), null, null, null, null, true, null, null));
				exam.setSection(new SectionEntity(sections.get(j).getId(), null, true, null, null, null, null, null, null));
				examRepo.saveAndFlush(exam);
				count = count + 1;
			}
		}

	}

	private void addMarksStatus(long schoolId, long examId) {
		List<ClassEntity> classes = classrepo.getClassessBySchool(schoolId);
		for (int k = 0; k < classes.size(); k++) {
			List<SectionEntity> sections = sectionRepo.getBySectionsBySchoolAndClass(schoolId, classes.get(k).getId());
			for (int h = 0; h < sections.size(); h++) {
				List<SubjectClassMapEntity> subjectClassMap = subjectClassMapRepository.getSubjectClassMapByClass(schoolId, classes.get(k).getId());
				for (int i = 0; i < subjectClassMap.size(); i++) {
					MarksStatusTrackerEntity marksCompTrackerNew = new MarksStatusTrackerEntity();
					marksCompTrackerNew.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
					marksCompTrackerNew.setExam(new ExamEntity(examId, null, null, null, null, null, null, false, null, null, null, null));
					marksCompTrackerNew.setCompleted(false);
					marksCompTrackerNew.setSubject(new SubjectEntity(subjectClassMap.get(i).getSubject().getId(), null, null, null, null, null, null));
					marksCompTrackerNew.setSection(new SectionEntity(sections.get(h).getId(), null, true, null, null, null, null, null, null));
					marksCompTrackerNew.setClasses(new ClassEntity(classes.get(k).getId(), null, null, null, null, true, null, null));
					marksCompTrackerNew.setLastModified(new Date());
					marksCompTrackerNew.setModifiedBy("dummy-dataloader");
					marksStatusRepo.saveAndFlush(marksCompTrackerNew);
				}
			}
		}
	}

	private void createExamTimeTable(long sclId) {

		List<ExamEntity> exams = examRepo.getExamsBySchool(sclId);
		for (int i = 0; i < exams.size(); i++) {

			if (exams.get(i).getScope().equals("School")) {
				List<ClassEntity> classes = classrepo.getClassessBySchool(sclId);
				for (int j = 0; j < classes.size(); j++) {

					List<SubjectClassMapEntity> subjects = subjectClassMapRepository.getSubjectClassMapByClass(sclId, classes.get(j).getId());
					int rand = (int) (Math.random() * ((30 - 5) + 1)) + 5;
					for (int k = 0; k < subjects.size(); k++) {
						saveExamTimeTable(DateUtils.addDays(new Date(System.currentTimeMillis()), -(rand - k)), sclId, classes.get(j).getId(), exams.get(i).getId(), subjects.get(k).getSubject().getId());
					}
					createExamNote(sclId, classes.get(j).getId(), exams.get(i).getId());

				}
			} else {
				List<SubjectClassMapEntity> subjects = subjectClassMapRepository.getSubjectClassMapByClass(sclId, exams.get(i).getClasses().getId());
				int rand = (int) (Math.random() * ((30 - 5) + 1)) + 5;
				Date date = DateUtils.addDays(new Date(System.currentTimeMillis()), +(rand));
				for (int k = 0; k < subjects.size(); k++) {
					saveExamTimeTable(date, sclId, exams.get(i).getClasses().getId(), exams.get(i).getId(), subjects.get(k).getSubject().getId());
				}
				createExamNote(sclId, exams.get(i).getClasses().getId(), exams.get(i).getId());
			}
		}
	}

	private void createExamNote(long sclId, long classId, long examId) {
		ExamNotesEntity examNotes = new ExamNotesEntity();
		examNotes.setCreatedDate(new Date());
		examNotes.setLastModified(new Date());
		examNotes.setNote("Exam notes " + examId + sclId + classId);
		examNotes.setModifiedBy("dummy-dataloader");
		if (classId == 0) {
			examNotes.setClasses(null);
		} else {
			examNotes.setClasses(new ClassEntity(classId, null, null, null, null, false, null, null));
		}
		examNotes.setSchool(new SchoolEntity(sclId, null, null, null, null, null, null, true, null, null, null, null, null, null));
		examNotes.setExam(new ExamEntity(examId, null, null, null, null, null, null, false, null, null, null, null));
		examNoteRepo.saveAndFlush(examNotes);
	}

	private void saveExamTimeTable(Date date, long schoolId, long classId, long examId, long subjectId) {
		ExamTimeTableEntity examTimetable = new ExamTimeTableEntity();
		examTimetable.setDate(date);
		examTimetable.setMarks(100);
		examTimetable.setExam(new ExamEntity(examId, null, null, null, null, null, null, false, null, null, null, null));
		examTimetable.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
		examTimetable.setClasses(new ClassEntity(classId, null, null, null, null, true, null, null));
		examTimetable.setSubject(new SubjectEntity(subjectId, null, null, null, null, null, null));
		examTimetable.setModifiedBy("dummy-dataloader");
		examTimetable.setCreatedDate(new Date());
		examTimeTableRepo.saveAndFlush(examTimetable);
	}

	private void addMarksBySubject(long sclId) {
		List<ExamEntity> exams = examRepo.getExamsBySchoolAndScope(sclId, "School");
		List<ClassEntity> classes = classrepo.getClassessBySchool(sclId);
		for (int i = 0; i < exams.size(); i++) {

			List<ExamTimeTableEntity> examTimeTable = examTimeTableRepo.getExamTimetableByExamId(exams.get(i).getId());
			for (int j = 0; j < examTimeTable.size(); j++) {

				for (int k = 0; k < classes.size(); k++) {
					List<StudentMapEntity> students = studentMapRepository.getByStudentsByClassId(classes.get(k).getId(), sclId);
					for (int s = 0; s < students.size(); s++) {

						int marksTemp = (int) (Math.random() * ((100 - 25) + 1)) + 23;
						MarksEntity mrks = marksRepo.getMarksBySclStuExamSubj(sclId, students.get(s).getStudent().getId(), exams.get(i).getId(), examTimeTable.get(j).getSubject().getId());
						MarksTrackerEntity marksTracker = marksTrackerRepo.getMarksByStudent(sclId, students.get(s).getStudent().getId(), exams.get(i).getId());
						if (mrks == null) {
							MarksEntity marksEntity = new MarksEntity();
							marksEntity.setMarks(marksTemp);
							marksEntity.setTotalMarks(examTimeTable.get(j).getMarks());
							marksEntity.setClasses(classes.get(k));
							marksEntity.setSchool(new SchoolEntity(sclId, null, null, null, null, null, null, false, null, null, null, null, null, null));
							marksEntity.setSubject(new SubjectEntity(examTimeTable.get(j).getSubject().getId(), null, null, null, null, null, null));
							marksEntity.setStudent(new StudentEntity(students.get(s).getStudent().getId(), null, null, null, null, null, null, null, null, null, null, null, null, null, false, false, null, null, null, null, null));
							marksEntity.setExam(new ExamEntity(exams.get(i).getId(), null, null, null, null, null, null, false, null, null, null, null));
							marksEntity.setModifiedBy("dummy-dataloader");
							marksEntity.setLastModified(new Date());
							marksRepo.saveAndFlush(marksEntity);

							/**
							 * Set Marks Status
							 */
							MarksStatusTrackerEntity marksStatus = marksStatusRepo.getSubjectMarksStatus(sclId, classes.get(k).getId(), students.get(s).getSection().getId(), examTimeTable.get(j).getSubject().getId(), exams.get(i).getId());
							marksStatus.setCompleted(true);
							marksStatusRepo.saveAndFlush(marksStatus);

							if (marksTracker == null) {
								MarksTrackerEntity marksTrackerNew = new MarksTrackerEntity();
								marksTrackerNew.setSchool(new SchoolEntity(sclId, null, null, null, null, null, null, true, null, null, null, null, null, null));
								marksTrackerNew.setStudent(new StudentEntity(students.get(s).getStudent().getId(), null, null, null, null, null, null, null, null, null, null, null, null, null, false, false, null, null, null, null, null));
								marksTrackerNew.setExam(new ExamEntity(exams.get(i).getId(), null, null, null, null, null, null, false, null, null, null, null));
								marksTrackerNew.setModifiedBy("dummy-dataloader");
								marksTrackerNew.setLastModified(new Date());
								marksTrackerNew.setMarks(marksTemp);
								marksTrackerNew.setMarksTotal(examTimeTable.get(j).getMarks());
								marksTrackerRepo.saveAndFlush(marksTrackerNew);
							} else {
								marksTracker.setModifiedBy("dummy-dataloader");
								marksTracker.setLastModified(new Date());
								marksTracker.setMarks(marksTracker.getMarks() + marksTemp);
								marksTracker.setMarksTotal(marksTracker.getMarksTotal() + examTimeTable.get(j).getMarks());
								marksTrackerRepo.saveAndFlush(marksTracker);
							}
						}
					}
				}

			}
		}
	}

	public void loadDailyTasks(long schoolId) throws ParseException {
		int skip = 0;
		String strDate = dateFormat.format(new Date());
		
		InitializationTrackerEntity initTracker;
		initTracker = initiRepo.getMarksBySclStuExamSubj("datacreation");
		initTracker.setStatus(true);
		initTracker.setLastModified(new Date());
		initTracker.setModifiedBy("su");
		initiRepo.saveAndFlush(initTracker);
		
		//Announcement
		AnnouncementEntity announcement = new AnnouncementEntity();
		announcement.setTitle("Announcement "+ new Date());
		announcement.setDescription("Announcement Desc "+ new Date());
		announcement.setLastModified(new Date());
		announcement.setModifiedBy("dummy-dataloader");
		announcement.setVisibilty(true);
		announcement.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
		announcementRepo.saveAndFlush(announcement);
				
		WorkingDayEntity day = dayRepo.getWorkingDayByDate(schoolId, dateFormat.parse(strDate));
		if (day == null) {
			day = dayRepo.saveAndFlush(new WorkingDayEntity(0, dateFormat.parse(strDate), "dummy-dataloader", new Date(), new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null), null));
			attendanceTrackerService.createEntriesForAttendenceTracker(day.getId(), schoolId);
		}

		List<ClassEntity> classes = classrepo.getClassessBySchool(schoolId);
		for (int i = 0; i < classes.size(); i++) {
			List<SectionEntity> section = sectionRepo.getBySectionsBySchoolAndClass(schoolId, classes.get(i).getId());
			for (int j = 0; j < section.size(); j++) {
				skip = 0;
				//Attendence
				List<StudentMapEntity> students = studentMapRepository.getByStudentsByClassId(classes.get(i).getId(), schoolId);
				for (int s = 0; s < students.size(); s++) {
					AttendanceEntity student = attendanceRepo.getAttendanceByClassAndSectionAndStudent(schoolId, classes.get(i).getId(), section.get(j).getId(), day.getId(), students.get(s).getId());
					if (student == null) {
						AttendanceEntity attendanceNew = new AttendanceEntity();
						attendanceNew.setAbsent(true);
						attendanceNew.setNote(null);
						attendanceNew.setLastModified(new Date());
						attendanceNew.setModifiedBy("dummy-dataloader");
						attendanceNew.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
						attendanceNew.setClasses(new ClassEntity(classes.get(i).getId(), null, null, null, null, true, null, null));
						attendanceNew.setSection(new SectionEntity(section.get(j).getId(), null, true, null, null, null, null, null, null));
						attendanceNew.setStudent(new StudentEntity(students.get(s).getId(), null, null, null, null, null, null, null, null, null, null, null, null, null, true, true, null, null, null, null, null));
						attendanceNew.setDay(day);
						attendanceNew.setNote("absent because of fever");
						attendanceRepo.saveAndFlush(attendanceNew);
					}
					skip = skip + 1;
					if(skip == 3) {
						break;
					}
				}
				AttendenceTrackerEntity tracker = attendenceTrackerRepo.getAttendanceTrackerByClassAndSection(schoolId, classes.get(i).getId(), section.get(j).getId(), day.getId());
				if (!tracker.isCompleted()) {
					tracker.setCompleted(true);
					attendenceTrackerRepo.saveAndFlush(tracker);
				}
				
				//Syllabus Status Updates
				List<Object[]> subjectsMap = subjectClassMapRepository.getSubjectsMappedToSchoolClass(schoolId, classes.get(i).getId());
				for (Object[] result : subjectsMap) {
					SyllabusEntity syllabusResp = syllabusRepo.getSyllabusBySection(schoolId, classes.get(i).getId(), section.get(j).getId(), (long) result[3]);
					if (syllabusResp == null) {
						SyllabusEntity syllabus =new SyllabusEntity();
						syllabus.setDescription("desc "+ new Date());
						syllabus.setPercentage(1);
						syllabus.setLastModified(new Date());
						syllabus.setModifiedBy("dummy-dataloader");
						syllabus.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
						syllabus.setClasses(new ClassEntity(classes.get(i).getId(), null, null, null, null, false, null, null));
						syllabus.setSection(new SectionEntity(section.get(j).getId(), null, true, null, null, null, null, null, null));
						syllabus.setSubject(new SubjectEntity((long) result[3], null, null, null, null, null, null));
						syllabusRepo.saveAndFlush(syllabus);
					} else {
						syllabusResp.setDescription(syllabusResp.getDescription()+"\r\n"+"desc "+ new Date());
						syllabusResp.setPercentage(syllabusResp.getPercentage()+1);
						syllabusResp.setLastModified(new Date());
						syllabusResp.setModifiedBy("dummy-dataloader");
						syllabusRepo.saveAndFlush(syllabusResp);
					}
				}
			}
		}
		
		initTracker = initiRepo.getMarksBySclStuExamSubj("datacreation");
		initTracker.setStatus(false);
		initTracker.setLastModified(new Date());
		initTracker.setModifiedBy("su");
		examService.checkExamCompletionStatus();
		initiRepo.saveAndFlush(initTracker);

	}

}
