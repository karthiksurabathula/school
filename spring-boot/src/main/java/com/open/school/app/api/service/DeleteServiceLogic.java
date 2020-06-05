package com.open.school.app.api.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.AnnouncementEntity;
import com.open.school.app.api.entity.AttendanceEntity;
import com.open.school.app.api.entity.AttendenceTrackerEntity;
import com.open.school.app.api.entity.ClassEntity;
import com.open.school.app.api.entity.ExamEntity;
import com.open.school.app.api.entity.ExamNotesEntity;
import com.open.school.app.api.entity.ExamTimeTableEntity;
import com.open.school.app.api.entity.InitializationTrackerEntity;
import com.open.school.app.api.entity.MarksEntity;
import com.open.school.app.api.entity.MarksStatusTrackerEntity;
import com.open.school.app.api.entity.MarksTrackerEntity;
import com.open.school.app.api.entity.OutboundEntity;
import com.open.school.app.api.entity.PeriodEntity;
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
import com.open.school.app.api.repository.ClassRepository;
import com.open.school.app.api.repository.ExamNotesRepository;
import com.open.school.app.api.repository.ExamRepository;
import com.open.school.app.api.repository.ExamTimeTableRepository;
import com.open.school.app.api.repository.InitializationTrackerRepository;
import com.open.school.app.api.repository.MarksRepository;
import com.open.school.app.api.repository.MarksStatusTrackerRepository;
import com.open.school.app.api.repository.MarksTrackerRepository;
import com.open.school.app.api.repository.OutboundRepository;
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
import com.open.school.app.jwt.entity.LoginUser;
import com.open.school.app.jwt.entity.UserOrgEntity;
import com.open.school.app.jwt.repository.LoginUserRepository;
import com.open.school.app.jwt.repository.UserOrgRepository;

@Service
public class DeleteServiceLogic {

	private static final Logger log = LogManager.getLogger(DeleteServiceLogic.class);

	@Autowired
	private AnnouncementRepository announcementRepo;
	@Autowired
	private ExamNotesRepository examNoteRepo;
	@Autowired
	private MarksRepository marksRepo;
	@Autowired
	private MarksStatusTrackerRepository marksStatusTrackerRepo;
	@Autowired
	private MarksTrackerRepository marksTrackerRepo;
	@Autowired
	private ExamTimeTableRepository examTimeTableRepo;
	@Autowired
	private ExamRepository examRepo;
	@Autowired
	private StudentRepository studentRepo;
	@Autowired
	private StudentMapRepository studentMapRepository;
	@Autowired
	private LoginUserRepository loginUserRepo;
	@Autowired
	private UserOrgRepository userOrgRepository;
	@Autowired
	private AttendanceRepository attendanceRepo;
	@Autowired
	private SubjectStaffMapRepository subjectStaffMapRepository;
	@Autowired
	private StaffRepository staffRepo;
	@Autowired
	private SubjectClassMapRepository subjectClassMapRepository;
	@Autowired
	private SubjectRepository subjectRepo;
	@Autowired
	private TimeTableRepository timetableRepo;
	@Autowired
	private PeriodRepository periodRepo;
	@Autowired
	private SyllabusRepository syllabusRepo;
	@Autowired
	private AttendanceRepository attenceRepo;
	@Autowired
	private AttendenceTrackerRepository attendenceTrackerRepo;
	@Autowired
	private SectionRepository sectionRepo;
	@Autowired
	private ClassRepository classrepo;
	@Autowired
	private SchoolRepository schoolRepo;
	@Autowired
	private OutboundRepository outboudRepo;
	@Autowired
	private InitializationTrackerRepository initRepo;
	@Autowired
	private WorkingDayRepository workDayRepo;
	@Autowired
	private SchoolCityRepository cityRepo;
	@Autowired
	private InitializationTrackerRepository initiRepo;

	public boolean deleteCity(long cityId) {
		try {
			List<SchoolEntity> school = schoolRepo.getBySchoolByCityid(cityId);
			for (int i = 0; i < school.size(); i++) {
				deleteSchool(school.get(i).getId());
			}
			cityRepo.deleteById(cityId);
		} catch (Exception e) {
			log.error("", e);
		}
		return true;
	}

	public boolean deleteSchool(long schoolId) {
		try {
			List<ClassEntity> classes = classrepo.getClassessBySchool(schoolId);
			for (int i = 0; i < classes.size(); i++) {
				deleteClasss(schoolId, classes.get(i).getId());
			}
			List<PeriodEntity> periods = periodRepo.getPeriodsBySchool(schoolId);
			for (int i = 0; i < periods.size(); i++) {
				deletePeriod(schoolId, periods.get(i).getId());
			}
			List<StaffEntity> staff = staffRepo.getByStaffBySchool(schoolId);
			for (int i = 0; i < staff.size(); i++) {
				deleteStaff(staff.get(i).getId());
			}
			List<StudentEntity> students = studentRepo.getByStudentsBySchool(schoolId);
			for (int i = 0; i < students.size(); i++) {
				deleteStudent(students.get(i).getId());
			}
			List<SubjectEntity> subjects = subjectRepo.getSubjectsBySchool(schoolId);
			for (int i = 0; i < subjects.size(); i++) {
				deleteSubject(schoolId, subjects.get(i).getId());
			}
			List<OutboundEntity> outboud = outboudRepo.getByBySchoolId(schoolId);
			for (int i = 0; i < outboud.size(); i++) {
				outboudRepo.delete(outboud.get(i));
			}
			List<ExamEntity> exam1 = examRepo.getExamsBySchoolAndScope(schoolId, "School");
			for (int i = 0; i < exam1.size(); i++) {
				deleteExam(exam1.get(i).getId());
			}
			List<AnnouncementEntity> announcements = announcementRepo.getAnnouncement(schoolId);
			for (int i = 0; i < announcements.size(); i++) {
				announcementRepo.delete(announcements.get(i));
			}
			List<WorkingDayEntity> workingDay = workDayRepo.getWorkingDayBySchool(schoolId);
			for (int i = 0; i < workingDay.size(); i++) {
				workDayRepo.delete(workingDay.get(i));
			}

			List<InitializationTrackerEntity> init = initRepo.getInitBySchool(schoolId);
			for (int i = 0; i < init.size(); i++) {
				initRepo.delete(init.get(i));
			}
			List<UserOrgEntity> users = userOrgRepository.findUserBySchool(schoolId);
			for (int i = 0; i < users.size(); i++) {
				userOrgRepository.delete(users.get(i));
			}
			List<LoginUser> user = loginUserRepo.findUserBySchool(schoolId);
			for (int i = 0; i < user.size(); i++) {
				loginUserRepo.delete(user.get(i));
			}

			schoolRepo.delete(schoolRepo.getBySchoolById(schoolId));
		} catch (Exception e) {
			log.error("", e);
		}
		return true;
	}

	public boolean deleteClasss(long schoolId, long classId) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			List<SectionEntity> section = sectionRepo.getBySectionsByClassId(classId);
			for (int i = 0; i < section.size(); i++) {
				deleteSection(section.get(i).getId(), schoolId);
			}

			List<SubjectClassMapEntity> subClassMap = subjectClassMapRepository.getSubjectClassMapByClassId(classId);
			for (int i = 0; i < subClassMap.size(); i++) {
				subjectClassMapRepository.delete(subClassMap.get(i));
			}

			List<ExamEntity> exam = examRepo.getExamsByClassId(classId);
			for (int i = 0; i < exam.size(); i++) {
				deleteExam(exam.get(i).getId());
			}

			List<ExamEntity> exam1 = examRepo.getExamsBySchoolAndScope(schoolId, "School");
			for (int i = 0; i < exam1.size(); i++) {
				deleteExamDown(exam1.get(i).getId());
			}

			classrepo.deleteById(classId);

			if (classrepo.getClassessBySchool(schoolId).size() == 0) {
				InitializationTrackerEntity initTracker = initiRepo.getMarksBySclStuExamSubj(schoolId, "class");
				initTracker.setStatus(false);
				initTracker.setLastModified(new Date());
				initTracker.setModifiedBy(auth.getName());
				initiRepo.saveAndFlush(initTracker);
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return true;
	}

	public boolean deleteSection(long sectionId, long schoolId) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			List<StudentMapEntity> studentsMap = studentMapRepository.getBySectionId(sectionId);
			for (int i = 0; i < studentsMap.size(); i++) {
				studentMapRepository.delete(studentsMap.get(i));
			}

			List<SubjectStaffMapEntity> subStaffMap = subjectStaffMapRepository.getBySectionId(sectionId);
			for (int i = 0; i < subStaffMap.size(); i++) {
				subjectStaffMapRepository.delete(subStaffMap.get(i));
			}

			List<SyllabusEntity> syllabus = syllabusRepo.getSyllabusBySectionId(sectionId);
			for (int i = 0; i < syllabus.size(); i++) {
				syllabusRepo.delete(syllabus.get(i));
			}

			List<TimeTableEntity> timetable = timetableRepo.getTimeTableBySectionId(sectionId);
			for (int i = 0; i < timetable.size(); i++) {
				timetableRepo.delete(timetable.get(i));
			}

			List<AttendenceTrackerEntity> attendenceTracker = attendenceTrackerRepo.getAttendanceTrackerBySectionId(sectionId);
			for (int i = 0; i < attendenceTracker.size(); i++) {
				attendenceTrackerRepo.delete(attendenceTracker.get(i));
			}

			List<AttendanceEntity> attendence = attenceRepo.getAttendanceBySectionId(sectionId);
			for (int i = 0; i < attendence.size(); i++) {
				attenceRepo.delete(attendence.get(i));
			}

			// Marks Status Tracker
			List<MarksStatusTrackerEntity> marksStatusTracker = marksStatusTrackerRepo.getSubjectMarksStatusBySectionId(sectionId);
			for (int i = 0; i < marksStatusTracker.size(); i++) {
				marksStatusTrackerRepo.delete(marksStatusTracker.get(i));
			}

			List<ExamEntity> exam = examRepo.getExamsBySectionAndScope(sectionId, "Section");
			for (int i = 0; i < exam.size(); i++) {
				deleteExam(exam.get(i).getId());
			}

			Optional<SectionEntity> section = sectionRepo.findById(sectionId);

			if (sectionRepo.getBySectionsBySchoolAndClass(schoolId, section.get().getClasses().getId()).size() == 0) {
				InitializationTrackerEntity initTracker = initiRepo.getMarksBySclStuExamSubj(schoolId, "section");
				initTracker.setStatus(false);
				initTracker.setLastModified(new Date());
				initTracker.setModifiedBy(auth.getName());
				initiRepo.saveAndFlush(initTracker);
			}

			sectionRepo.delete(section.get());
		} catch (Exception e) {
			log.error("", e);
		}
		return true;
	}

	public boolean deleteStudent(long studentId) {
		try {
			// Marks Tracker
			List<MarksTrackerEntity> marksTracker = marksTrackerRepo.getMarksByStudentId(studentId);
			for (int i = 0; i < marksTracker.size(); i++) {
				marksTrackerRepo.delete(marksTracker.get(i));
			}
			// Marks
			List<MarksEntity> marksList = marksRepo.getMarksByStudentId(studentId);
			for (int i = 0; i < marksList.size(); i++) {
				marksRepo.delete(marksList.get(i));
			}
			// Attendence
			List<AttendanceEntity> attendence = attendanceRepo.getAttendanceByStudent(studentId);
			for (int i = 0; i < attendence.size(); i++) {
				attendanceRepo.delete(attendence.get(i));
			}
			// Student Map
			StudentMapEntity stuMap = studentMapRepository.getByStuId(studentId);
			if(stuMap!=null) {
				studentMapRepository.delete(stuMap);
			}
			Optional<StudentEntity> stu = studentRepo.findById(studentId);
			// User group
			userOrgRepository.delete(userOrgRepository.findUserOrgMapByUsername(stu.get().getUsername()));
			// login user
			loginUserRepo.delete(loginUserRepo.findByUsername(stu.get().getUsername()));
			// student
			studentRepo.delete(stu.get());
		} catch (Exception e) {
			log.error("", e);
		}
		return true;
	}

	public boolean deleteSubject(long schoolId, long subjectId) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			List<SubjectClassMapEntity> subjectMap = subjectClassMapRepository.getSubjectClassMapBySubjectId(schoolId, subjectId);
			for (int i = 0; i < subjectMap.size(); i++) {
				deleteSubjectClassMap(schoolId, subjectMap.get(i).getSubject().getId(), subjectMap.get(i).getClasses().getId());
			}

			SyllabusEntity syllabus = syllabusRepo.getSyllabusBySubjectId(subjectId);
			if (syllabus != null) {
				syllabusRepo.delete(syllabus);
			}
			SubjectEntity subject = subjectRepo.findById(subjectId).get();
			if (subject != null) {
				subjectRepo.delete(subject);
			}

			if (subjectRepo.getSubjectsBySchool(schoolId).size() == 0) {
				InitializationTrackerEntity initTracker = initiRepo.getMarksBySclStuExamSubj(schoolId, "subject");
				initTracker.setStatus(false);
				initTracker.setLastModified(new Date());
				initTracker.setModifiedBy(auth.getName());
				initiRepo.saveAndFlush(initTracker);
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return true;
	}

	public boolean deletePeriod(long schoolId, long periodId) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			List<TimeTableEntity> timeTable = timetableRepo.getTimeTableByPeriodId(schoolId, periodId);
			for (int i = 0; i < timeTable.size(); i++) {
				timetableRepo.delete(timeTable.get(i));
			}
			periodRepo.deleteById(periodId);

			if (periodRepo.getPeriodsBySchool(schoolId).size() == 0) {
				InitializationTrackerEntity initTracker = initiRepo.getMarksBySclStuExamSubj(schoolId, "period");
				initTracker.setStatus(false);
				initTracker.setLastModified(new Date());
				initTracker.setModifiedBy(auth.getName());
				initiRepo.saveAndFlush(initTracker);
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return true;
	}

	public boolean deleteStaff(long staffId) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			List<SubjectStaffMapEntity> staffMap = subjectStaffMapRepository.getByStaffId(staffId);
			for (int i = 0; i < staffMap.size(); i++) {
				subjectStaffMapRepository.delete(staffMap.get(i));
			}
			StaffEntity staff = staffRepo.findById(staffId).get();

			if (staffRepo.getByStaffBySchool(staff.getSchool().getId()).size() == 0) {
				InitializationTrackerEntity initTracker = initiRepo.getMarksBySclStuExamSubj(staff.getSchool().getId(), "staff");
				initTracker.setStatus(false);
				initTracker.setLastModified(new Date());
				initTracker.setModifiedBy(auth.getName());
				initiRepo.saveAndFlush(initTracker);
			}

			if (staff != null) {
				userOrgRepository.delete(userOrgRepository.findUserOrgMapByUsername(staff.getStaffId()));
				// login user
				loginUserRepo.delete(loginUserRepo.findByUsername(staff.getStaffId()));
				staffRepo.delete(staff);
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return true;
	}

	// OK
	public boolean deleteAnnouncement(long id) {
		try {
			announcementRepo.deleteById(id);
		} catch (Exception e) {
			log.error("", e);
		}
		return true;
	}

	public boolean deleteExam(long examId) {
		try {
			deleteExamDown(examId);
			examRepo.deleteById(examId);
		} catch (Exception e) {
			log.error("", e);
		}
		return true;
	}

	public boolean deleteExamDown(long examId) {
		try {
			// Exam Note
			List<ExamNotesEntity> note = examNoteRepo.getExamNotesByExamId(examId);
			for (int i = 0; i < note.size(); i++) {
				examNoteRepo.delete(note.get(i));
			}

			// Marks
			List<MarksEntity> marksList = marksRepo.getMarksByExamId(examId);
			for (int i = 0; i < marksList.size(); i++) {
				marksRepo.delete(marksList.get(i));
			}
			// Marks Tracker
			List<MarksTrackerEntity> marksTracker = marksTrackerRepo.getMarksByExamId(examId);
			for (int i = 0; i < marksTracker.size(); i++) {
				marksTrackerRepo.delete(marksTracker.get(i));
			}
			// Marks Status Tracker
			List<MarksStatusTrackerEntity> marksStatusTracker = marksStatusTrackerRepo.getSubjectMarksStatusByExamId(examId);
			for (int i = 0; i < marksStatusTracker.size(); i++) {
				marksStatusTrackerRepo.delete(marksStatusTracker.get(i));
			}
			// Exam Time table
			List<ExamTimeTableEntity> timetable = examTimeTableRepo.getExamTimetableByExamId(examId);
			for (int i = 0; i < timetable.size(); i++) {
				examTimeTableRepo.delete(timetable.get(i));
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return true;
	}

	public boolean deleteSubjectClassMap(long schoolId, long subjectId, long classId) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			// Marks
			List<MarksEntity> marksList = marksRepo.getMarksBySubjectAndClassId(schoolId, subjectId, classId);
			for (int i = 0; i < marksList.size(); i++) {
				MarksTrackerEntity marks = marksTrackerRepo.getMarksByStudentAndExamId(schoolId, marksList.get(i).getExam().getId(), marksList.get(i).getStudent().getId());
				marks.setMarks(marks.getMarks() - marksList.get(i).getMarks());
				marks.setMarksTotal(marks.getMarksTotal() - marksList.get(i).getTotalMarks());
				marksTrackerRepo.saveAndFlush(marks);

				// Marks Status Tracker
				List<MarksStatusTrackerEntity> marksStatusTracker = marksStatusTrackerRepo.getSubjectMarksStatusBySubjectAndClassAndExamId(schoolId, subjectId, classId, marksList.get(i).getExam().getId());
				for (int j = 0; j < marksStatusTracker.size(); j++) {
					marksStatusTrackerRepo.delete(marksStatusTracker.get(j));
				}

				marksRepo.delete(marksList.get(i));
			}

			List<SubjectStaffMapEntity> subStaffMap = subjectStaffMapRepository.getBySubjectAndClassId(schoolId, subjectId, classId);
			for (int i = 0; i < subStaffMap.size(); i++) {
				subjectStaffMapRepository.delete(subStaffMap.get(i));
			}

			// Exam Time table
			List<ExamTimeTableEntity> timetable = examTimeTableRepo.getExamTimetableBySubjectAnClassId(schoolId, subjectId, classId);
			for (int i = 0; i < timetable.size(); i++) {
				examTimeTableRepo.delete(timetable.get(i));
			}

			List<TimeTableEntity> timeTable = timetableRepo.getTimeTableBySubjectAndClassId(schoolId, subjectId, classId);
			for (int i = 0; i < timeTable.size(); i++) {
				timetableRepo.delete(timeTable.get(i));
			}

			List<SubjectClassMapEntity> subjClassMap = subjectClassMapRepository.getSubjectClassMapBySubjectAndClassId(schoolId, subjectId, classId);
			for (int i = 0; i < subjClassMap.size(); i++) {
				subjectClassMapRepository.delete(subjClassMap.get(i));
			}

			if (subjectClassMapRepository.getSubjectClassMapByClass(schoolId, classId).size() == 0) {
				InitializationTrackerEntity initTracker = initiRepo.getMarksBySclStuExamSubj(schoolId, "subjectClass");
				initTracker.setStatus(false);
				initTracker.setLastModified(new Date());
				initTracker.setModifiedBy(auth.getName());
				initiRepo.saveAndFlush(initTracker);
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return true;
	}

}
