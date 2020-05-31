package com.open.school.app.api.service;

//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.open.school.app.api.entity.ClassEntity;
import com.open.school.app.api.entity.ExamEntity;
import com.open.school.app.api.entity.ExamTimeTableEntity;
import com.open.school.app.api.entity.MarksEntity;
import com.open.school.app.api.entity.MarksStatusTrackerEntity;
import com.open.school.app.api.entity.MarksTrackerEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.entity.StudentEntity;
import com.open.school.app.api.entity.StudentMapEntity;
import com.open.school.app.api.entity.SubjectEntity;
import com.open.school.app.api.model.marks.MarksReqModel;
import com.open.school.app.api.repository.ExamTimeTableRepository;
import com.open.school.app.api.repository.MarksRepository;
import com.open.school.app.api.repository.MarksStatusTrackerRepository;
import com.open.school.app.api.repository.MarksTrackerRepository;
import com.open.school.app.api.repository.StudentMapRepository;

@Service
public class MarksServiceImpl {

	@Autowired
	private MarksRepository marksRepo;
	@Autowired
	private StudentMapRepository studentMapRepo;
	@Autowired
	private ExamTimeTableRepository examTimeTableRepo;
	@Autowired
	private MarksTrackerRepository marksTrackerRepo;
	@Autowired
	private MarksStatusTrackerRepository marksStatusRepo;

	private static final Logger log = LogManager.getLogger(MarksServiceImpl.class);

	public ResponseEntity<?> getMarkseBySchoolClassSectionAndSubject(long schoolId, long examId, long subjectId, long classId, long sectionId) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> marksResp = new ArrayList<Object>();

		String message = null;
		try {
			List<MarksEntity> marks = marksRepo.getMarksByExamAndSubject(schoolId, subjectId, examId);
			ExamTimeTableEntity examTimeTable = examTimeTableRepo.getExamTimetableBySubject(schoolId, classId, examId, subjectId);
			if (marks.size() > 0) {
				for (int i = 0; i < marks.size(); i++) {
					StudentEntity stu = marks.get(i).getStudent();

					StudentMapEntity stuMap = studentMapRepo.getByStudentId(stu.getId(), schoolId);
					if (stuMap.getSection().getId() == sectionId) {
						MarksReqModel marksRespTemp = new MarksReqModel();
						marksRespTemp.setMarks(marks.get(i));
						marksRespTemp.setStudent(new StudentEntity(stu.getId(), stu.getStudentId(), stu.getName(), null, null, null, null, null, null, null, null, null, null, message, false, false, null, null, null, null, null));
						marksResp.add(marksRespTemp);
					}

				}
			} else {
				List<Object[]> stuDet = studentMapRepo.getByStudentsDetailsByClass(schoolId, classId, sectionId);
				for (Object[] result : stuDet) {
					MarksReqModel marksRespTemp = new MarksReqModel();
					marksRespTemp.setMarks(new MarksEntity(0, 0, 0, null, null, null, null, null, null, null));
					marksRespTemp.setStudent(new StudentEntity((long) result[0], (String) result[1], (String) result[2], null, null, null, null, null, null, null, null, null, null, message, false, false, null, null, null, null, null));
					marksResp.add(marksRespTemp);
				}
			}
			response.put("data", marksResp);
			response.put("timetable", examTimeTable);
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

	public ResponseEntity<?> getMarkseByStudentSchoolAndExam(long schoolId, long examId, long studentId) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> marksResp = new ArrayList<Object>();

		String message = null;
		try {
			List<MarksEntity> marks = marksRepo.getMarksByExamAndStudent(schoolId, studentId, examId);
			if (marks.size() > 0) {
				for (int i = 0; i < marks.size(); i++) {
					StudentEntity stu = marks.get(i).getStudent();
					MarksReqModel marksRespTemp = new MarksReqModel();
					marksRespTemp.setMarks(marks.get(i));
					marksRespTemp.setSubject(marks.get(i).getSubject());
					marksRespTemp.setStudent(new StudentEntity(stu.getId(), stu.getStudentId(), stu.getName(), null, null, null, null, null, null, null, null, null, null, null, false, false, null, null, null, null, null));
					marksResp.add(marksRespTemp);
				}
				response.put("data", marksResp);
				response.put("indicator", "success");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("indicator", "fail");
				response.put("message", "Marks not found");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

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

	public ResponseEntity<?> saveMarksBySubject(long schoolId, long examId, long subjectId, long classId, long sectionId, List<MarksReqModel> marks) {
		HashMap<String, Object> response = new HashMap<>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String message = null;
		try {

			ExamTimeTableEntity exam = examTimeTableRepo.getExamTimetableBySubject(schoolId, classId, examId, subjectId);

			Calendar c = Calendar.getInstance();
			Calendar c1 = Calendar.getInstance();
			
			//Exam date
			c.setTime(exam.getDate());
			c.add(Calendar.DATE, +1); //Allow marks entry from next day of exam
			
			//Current Date
			c1.setTime(new Date());	
						
//			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//			System.out.println(c.getTime());
//			System.out.println(c1.getTime());
//			
//			System.out.println(c.before(c1));
			
			if (c.before(c1)) {
				for (int i = 0; i < marks.size(); i++) {
					MarksEntity mrks = marksRepo.getMarksBySclStuExamSubj(schoolId, marks.get(i).getStudent().getId(), examId, subjectId);
					MarksTrackerEntity marksTracker = marksTrackerRepo.getMarksByStudent(schoolId, marks.get(i).getStudent().getId(), examId);
					ExamTimeTableEntity examTimeTable = examTimeTableRepo.getExamTimetableBySubject(schoolId, classId, examId, subjectId);
					if (examTimeTable != null) {
						if (mrks == null) {
							MarksEntity marksEntity = new MarksEntity();
							marksEntity.setMarks(marks.get(i).getMarks().getMarks());
							marksEntity.setTotalMarks(examTimeTable.getMarks());
							marksEntity.setClasses(new ClassEntity(classId, null, null, null, null, false, null, null));
							marksEntity.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, false, null, null, null, null, null, null));
							marksEntity.setSubject(new SubjectEntity(subjectId, null, null, null, null, null, null));
							marksEntity.setStudent(new StudentEntity(marks.get(i).getStudent().getId(), null, null, null, null, null, null, null, null, null, null, null, null, null, false, false, null, null, null, null, null));
							marksEntity.setExam(new ExamEntity(examId, null, null, null, null, null, null, false, null, null, null, null));
							marksEntity.setModifiedBy(auth.getName());
							marksEntity.setLastModified(new Date());
							marksRepo.saveAndFlush(marksEntity);

							/**
							 * Set Marks Status
							 */
							MarksStatusTrackerEntity marksStatus = marksStatusRepo.getSubjectMarksStatus(schoolId, classId, sectionId, subjectId, examId);
							marksStatus.setCompleted(true);
							marksStatusRepo.saveAndFlush(marksStatus);

							if (marksTracker == null) {
								MarksTrackerEntity marksTrackerNew = new MarksTrackerEntity();
								marksTrackerNew.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
								marksTrackerNew.setStudent(new StudentEntity(marks.get(i).getStudent().getId(), null, null, null, null, null, null, null, null, null, null, null, null, null, false, false, null, null, null, null, null));
								marksTrackerNew.setExam(new ExamEntity(examId, null, null, null, null, null, null, false, null, null, null, null));
								marksTrackerNew.setModifiedBy(auth.getName());
								marksTrackerNew.setLastModified(new Date());
								marksTrackerNew.setMarks(marks.get(i).getMarks().getMarks());
								marksTrackerNew.setMarksTotal(examTimeTable.getMarks());
								marksTrackerRepo.saveAndFlush(marksTrackerNew);
							} else {
								marksTracker.setModifiedBy(auth.getName());
								marksTracker.setLastModified(new Date());
								marksTracker.setMarks(marksTracker.getMarks() + marks.get(i).getMarks().getMarks());
								marksTracker.setMarksTotal(marksTracker.getMarksTotal() + examTimeTable.getMarks());
								marksTrackerRepo.saveAndFlush(marksTracker);
							}

						} else {

							marksTracker.setMarks((marksTracker.getMarks() - mrks.getMarks()) + marks.get(i).getMarks().getMarks());
							mrks.setMarks(marks.get(i).getMarks().getMarks());
							mrks.setTotalMarks(examTimeTable.getMarks());
							mrks.setModifiedBy(auth.getName());
							mrks.setLastModified(new Date());
							marksRepo.saveAndFlush(mrks);

							marksTracker.setModifiedBy(auth.getName());
							marksTracker.setLastModified(new Date());
							marksTrackerRepo.saveAndFlush(marksTracker);
						}
						response.put("indicator", "success");
						response.put("message", "Marks updated successfully");
					} else {
						response.put("indicator", "fail");
						response.put("message", "Exam Timetable not set");
					}
				}
			} else {
				response.put("indicator", "fail");
				response.put("message", "Marks cannot be added before exam date + 1");
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

}
