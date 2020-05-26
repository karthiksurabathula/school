package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.ExamNotesEntity;

@Repository
public interface ExamNotesRepository extends JpaRepository<ExamNotesEntity, Long> {

	@Query(value = "SELECT e FROM ExamNotesEntity e WHERE e.exam.id=:examId and e.school.id=:schoolId and e.classes.id=:classId")
	ExamNotesEntity getExamNotesByExamIdAndSchool(@Param("examId") long examId, @Param("schoolId") long schoolId, @Param("classId") long classId);

	@Query(value = "SELECT e FROM ExamNotesEntity e WHERE e.id=:examId and e.school.id=:schoolId")
	ExamNotesEntity getExamNote(@Param("examId") long examId, @Param("schoolId") long schoolId);

	@Query(value = "SELECT e FROM ExamNotesEntity e WHERE e.exam.id=:examId")
	List<ExamNotesEntity> getExamNotesByExamId(@Param("examId") long examId);
	
	@Query(value = "SELECT e FROM ExamNotesEntity e WHERE e.classes.id=:classId")
	List<ExamNotesEntity> getExamNotesByClassId(@Param("classId") long classId);
	
	

}
