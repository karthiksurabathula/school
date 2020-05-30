package com.open.school.app.api.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.ClassEntity;
import com.open.school.app.api.entity.ExamEntity;
import com.open.school.app.api.entity.ExamTimeTableEntity;
import com.open.school.app.api.entity.MarksStatusTrackerEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.entity.SectionEntity;
import com.open.school.app.api.entity.StaffEntity;
import com.open.school.app.api.entity.SubjectClassMapEntity;
import com.open.school.app.api.entity.SubjectEntity;
import com.open.school.app.api.repository.ClassRepository;
import com.open.school.app.api.repository.ExamRepository;
import com.open.school.app.api.repository.ExamTimeTableRepository;
import com.open.school.app.api.repository.MarksStatusTrackerRepository;
import com.open.school.app.api.repository.SectionRepository;
import com.open.school.app.api.repository.StaffRepository;
import com.open.school.app.api.repository.SubjectClassMapRepository;

@Service
public class ExamServiceImpl {

	@Autowired
	private ExamRepository examRepo;
	@Autowired
	private MarksStatusTrackerRepository marksStatusRepo;
	@Autowired
	private SubjectClassMapRepository subjectClassMapRepository;
	@Autowired
	private ClassRepository classrepo;
	@Autowired
	private SectionRepository sectionRepo;
	@Autowired
	private DeleteServiceLogic deleteService;
	@Autowired
	private ExamTimeTableRepository examTimetableRepo;
	@Autowired
	private StaffRepository staffRepo;
	
	private static final Logger log = LogManager.getLogger(ExamServiceImpl.class);

	public ResponseEntity<?> createExam(ExamEntity exam, long schoolId, long classId, long sectionId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			exam.setLastModified(new Date());
			exam.setCreatedDate(new Date());
			exam.setModifiedBy(auth.getName());
			exam.setSection(null);
			exam.setCompleted(false);

			if (exam.getScope().equals("Class")) {
				exam.setClasses(new ClassEntity(classId, null, null, null, null, true, null, null));
				exam.setSection(null);
			}
			if (exam.getScope().equals("Section")) {
				exam.setClasses(new ClassEntity(classId, null, null, null, null, true, null, null));
				exam.setSection(new SectionEntity(sectionId, null, true, null, null, null, null, null, null));
			}
			exam.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
			ExamEntity examResp = examRepo.saveAndFlush(exam);

			if (exam.getScope().equals("School")) {
				addMarksStatus(schoolId, examResp.getId());
			}

			response.put("exam", examResp);
			response.put("indicator", "success");
			response.put("message", exam.getName() + " Created successfully");
			
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

	@Async("asyncExecutor")
	private void addMarksStatus(long schoolId, long examId) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			List<ClassEntity> classes = classrepo.getClassessBySchool(schoolId);
			for (int k = 0; k < classes.size(); k++) {
				List<SectionEntity> sections = sectionRepo.getBySectionsBySchoolAndClass(schoolId, classes.get(k).getId());
				for (int h = 0; h < sections.size(); h++) {
					List<SubjectClassMapEntity> subject = subjectClassMapRepository.getSubjectClassMapByClass(schoolId, classes.get(k).getId());
					for (int i = 0; i < subject.size(); i++) {
						MarksStatusTrackerEntity marksCompTrackerNew = new MarksStatusTrackerEntity();
						marksCompTrackerNew.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
						marksCompTrackerNew.setExam(new ExamEntity(examId, null, null, null, null, null, null, false, null, null, null, null));
						marksCompTrackerNew.setCompleted(false);
						marksCompTrackerNew.setLastModified(new Date());
						marksCompTrackerNew.setModifiedBy(auth.getName());
						marksCompTrackerNew.setSubject(new SubjectEntity(subject.get(i).getSubject().getId(), null, null, null, null, null, null));
						marksCompTrackerNew.setSection(new SectionEntity(sections.get(h).getId(), null, true, null, null, null, null, null, null));
						marksCompTrackerNew.setClasses(new ClassEntity(classes.get(k).getId(), null, null, null, null, true, null, null));
						marksStatusRepo.saveAndFlush(marksCompTrackerNew);
					}
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}

	}

	public ResponseEntity<?> updateExam(ExamEntity exam, long schoolId, long classId, long sectionId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;

		try {
			ExamEntity ActExam = examRepo.getExamByIdAndSchool(exam.getId(), schoolId);
			if (ActExam != null) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				exam.setLastModified(new Date());
				exam.setCreatedDate(new Date());
				exam.setModifiedBy(auth.getName());

				exam.setSection(null);
				exam.setClasses(null);

				if (exam.getScope().equals("Class")) {
					exam.setClasses(new ClassEntity(classId, null, null, null, null, true, null, null));
					exam.setSection(null);
				}
				if (exam.getScope().equals("Section")) {
					exam.setClasses(new ClassEntity(classId, null, null, null, null, true, null, null));
					exam.setSection(new SectionEntity(sectionId, null, true, null, null, null, null, null, null));
				}

				exam.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));

				examRepo.saveAndFlush(exam);
				response.put("exam", examRepo);
				response.put("indicator", "success");
				response.put("message", exam.getName() + " Created successfully");
			} else {
				response.put("indicator", "fail");
				response.put("code", "E1007");
				response.put("message", "Exam Not found");
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

	public ResponseEntity<?> deleteExams(long examId, long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;

		try {
			ExamEntity exam = examRepo.getExamByIdAndSchool(examId, schoolId);
			if (exam != null) {
				deleteService.deleteExam(exam.getId());
//				examRepo.delete(exam);
				response.put("message", exam.getName() + " deleted Successfully");
				response.put("indicator", "success");
			} else {
				response.put("indicator", "fail");
				response.put("code", "E1007");
				response.put("message", "Exam not found");
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

	public ResponseEntity<?> getExamsBySchool(long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> examResponse = new ArrayList<Object>();

		String message = null;
		try {
			List<ExamEntity> exams = examRepo.getExamsBySchool(schoolId);
			if (exams.size() > 0) {
				response.put("indicator", "success");
				for (int i = 0; i < exams.size(); i++) {
					HashMap<String, Object> exam = new HashMap<>();

					exam.put("scope", exams.get(i).getScope());
					exam.put("exam", exams.get(i));
					exam.put("examName", exams.get(i).getName());
					exam.put("schoolId", exams.get(i).getSchool().getId());
					if (exams.get(i).getScope().equals("Class")) {
						exam.put("classId", exams.get(i).getClasses().getId());
						exam.put("className", exams.get(i).getClasses().getClassName());
					}
					if (exams.get(i).getScope().equals("Section")) {
						exam.put("classId", exams.get(i).getClasses().getId());
						exam.put("className", exams.get(i).getClasses().getClassName());
						exam.put("sectionId", exams.get(i).getSection().getId());
						exam.put("sectionName", exams.get(i).getSection().getSectionName());
					}
					examResponse.add(exam);
				}
				response.put("exams", examResponse);
			} else {
				response.put("indicator", "fail");
				response.put("message", "Exams not Found");
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

	public ResponseEntity<?> getExamByScopeSchool(long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> examResponse = new ArrayList<Object>();

		String message = null;
		try {
			List<ExamEntity> exams = examRepo.getExamsBySchoolAndScope(schoolId, "School");
			if (exams.size() > 0) {
				response.put("indicator", "success");
				for (int i = 0; i < exams.size(); i++) {
					HashMap<String, Object> exam = new HashMap<>();
					exam.put("scope", exams.get(i).getScope());
					exam.put("exam", exams.get(i));
					exam.put("examName", exams.get(i).getName());
					exam.put("schoolId", exams.get(i).getSchool().getId());
					if (exams.get(i).getScope().equals("Class")) {
						exam.put("classId", exams.get(i).getClasses().getId());
						exam.put("className", exams.get(i).getClasses().getClassName());
					}
					if (exams.get(i).getScope().equals("Section")) {
						exam.put("classId", exams.get(i).getClasses().getId());
						exam.put("className", exams.get(i).getClasses().getClassName());
						exam.put("sectionId", exams.get(i).getSection().getId());
						exam.put("sectionName", exams.get(i).getSection().getSectionName());
					}
					examResponse.add(exam);
				}
				response.put("exams", examResponse);
			} else {
				response.put("indicator", "fail");
				response.put("message", "Exams not Found");
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

	public ResponseEntity<?> getExamsBySchoolAndClassAndSection(long schoolId, long classId, long sectionId) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> examResponse = new ArrayList<Object>();

		String message = null;
		try {
			List<ExamEntity> exams = examRepo.getExamsBySchoolAndClassAndSection(schoolId, classId, sectionId);
			if (exams.size() > 0) {
				response.put("indicator", "success");
				for (int i = 0; i < exams.size(); i++) {
					HashMap<String, Object> exam = new HashMap<>();

					exam.put("scope", exams.get(i).getScope());
					exam.put("exam", exams.get(i));
					exam.put("examName", exams.get(i).getName());
					exam.put("schoolId", exams.get(i).getSchool().getId());
					if (exams.get(i).getScope().equals("Class")) {
						exam.put("classId", exams.get(i).getClasses().getId());
						exam.put("className", exams.get(i).getClasses().getClassName());
					}
					if (exams.get(i).getScope().equals("Section")) {
						exam.put("classId", exams.get(i).getClasses().getId());
						exam.put("className", exams.get(i).getClasses().getClassName());
						exam.put("sectionId", exams.get(i).getSection().getId());
						exam.put("sectionName", exams.get(i).getSection().getSectionName());
					}
					examResponse.add(exam);
				}
				response.put("exams", examResponse);
			} else {
				response.put("indicator", "fail");
				response.put("message", "Exams not Found");
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
	
	
	
	
	
	public ResponseEntity<?> getExamsByTeacher(long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object> examResponse = new ArrayList<Object>(); 
		String message = null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			StaffEntity staff = staffRepo.getByStaffBySchoolAndIdString(schoolId, auth.getName());
			List<ExamEntity> exams = examRepo.getExamsByStaff(schoolId,staff.getId());
			if (exams.size() > 0) {
				response.put("indicator", "success");
				for (int i = 0; i < exams.size(); i++) {
					HashMap<String, Object> exam = new HashMap<>();

					exam.put("scope", exams.get(i).getScope());
					exam.put("exam", exams.get(i));
					exam.put("examName", exams.get(i).getName());
					exam.put("schoolId", exams.get(i).getSchool().getId());
					if (exams.get(i).getScope().equals("Class")) {
						exam.put("classId", exams.get(i).getClasses().getId());
						exam.put("className", exams.get(i).getClasses().getClassName());
					}
					if (exams.get(i).getScope().equals("Section")) {
						exam.put("classId", exams.get(i).getClasses().getId());
						exam.put("className", exams.get(i).getClasses().getClassName());
						exam.put("sectionId", exams.get(i).getSection().getId());
						exam.put("sectionName", exams.get(i).getSection().getSectionName());
					}
					examResponse.add(exam);
				}
				response.put("exams", examResponse);
			} else {
				response.put("indicator", "fail");
				response.put("message", "Exams not Found");
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
	
	
	
	
	
	
	@Async("asyncExecutor")
	public void checkExamCompletionStatus() {
		boolean skip=true;
		List<ExamEntity> exams = examRepo.getExamsByCompletionStatus(false);
		for(int i=0;i<exams.size();i++) {
			List<ExamTimeTableEntity> timetable = examTimetableRepo.getExamTimetableByExamId(exams.get(i).getId());
			for(int j=0;j<timetable.size();j++) {
				if(timetable.get(j).getDate().after(new Date())) {
					skip=false;
					break;
				}
			}
			if(skip) {
				exams.get(i).setCompleted(true);
				examRepo.saveAndFlush(exams.get(i));
			}
			
		}
	}

}
