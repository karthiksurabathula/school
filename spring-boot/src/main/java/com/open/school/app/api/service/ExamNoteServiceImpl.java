package com.open.school.app.api.service;

import java.util.Date;
import java.util.HashMap;

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
import com.open.school.app.api.entity.ExamNotesEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.repository.ExamNotesRepository;

@Service
public class ExamNoteServiceImpl {

	@Autowired
	private ExamNotesRepository examNoteRepo;
	private static final Logger log = LogManager.getLogger(ExamNoteServiceImpl.class);

	public ResponseEntity<?> getNotesByExamAndSchool(long schoolId, long examId, long classId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			response.put("indicator", "success");
			ExamNotesEntity examNote = examNoteRepo.getExamNotesByExamIdAndSchool(examId, schoolId, classId);
			if (examNote == null) {
				response.put("examNotes", new ExamNotesEntity(0, null, null, null, null, null, null, null));
			} else {
				response.put("examNotes", examNote);
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1011");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> saveExamNotes(ExamNotesEntity examNotes, long schoolId, long examId, long classId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			ExamNotesEntity examNote = examNoteRepo.getExamNote(examNotes.getId(), schoolId);
			if (examNote == null) {
				examNotes.setCreatedDate(new Date());
			} else {
				examNotes.setCreatedDate(examNote.getCreatedDate());
			}
			examNotes.setLastModified(new Date());

			examNotes.setModifiedBy(auth.getName());
			if (classId == 0) {
				examNotes.setClasses(null);
			} else {
				examNotes.setClasses(new ClassEntity(classId, null, null, null, null, false, null, null));
			}
			examNotes.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
			examNotes.setExam(new ExamEntity(examId, null, null, null, null, null, null, false, null, null, null, null));
			response.put("examNotes", examNoteRepo.saveAndFlush(examNotes));
			response.put("indicator", "success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1013");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
