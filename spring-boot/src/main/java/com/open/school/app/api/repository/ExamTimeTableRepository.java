package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.ExamTimeTableEntity;

@Repository
public interface ExamTimeTableRepository extends JpaRepository<ExamTimeTableEntity, Long> {

	@Query(value = "SELECT e FROM ExamTimeTableEntity e WHERE e.school.id=:schoolId and e.classes.id=:classId and e.exam.id=:examId")
	List<ExamTimeTableEntity> getExamTimetableByClass(@Param("schoolId") long schoolId, @Param("classId") long classId, @Param("examId") long examId);

	@Query(value = "SELECT e FROM ExamTimeTableEntity e WHERE e.school.id=:schoolId and e.exam.id=:examId")
	List<ExamTimeTableEntity> getExamTimetableOfSchoolByExam(@Param("schoolId") long schoolId, @Param("examId") long examId);

	@Query(value = "SELECT e FROM ExamTimeTableEntity e WHERE e.id=:examTimetableId and e.school.id=:schoolId and e.classes.id=:classId and e.exam.id=:examId and e.exam.id=:examId")
	ExamTimeTableEntity getExamTimetableById(@Param("examTimetableId") long examTimetableId, @Param("schoolId") long schoolId, @Param("classId") long classId, @Param("examId") long examId);

	@Query(value = "SELECT e FROM ExamTimeTableEntity e WHERE e.school.id=:schoolId and e.classes.id=:classId and e.exam.id=:examId and e.subject.id=:subjectId")
	ExamTimeTableEntity getExamTimetableBySubject(@Param("schoolId") long schoolId, @Param("classId") long classId, @Param("examId") long examId, @Param("subjectId") long subjectId);

	//
	
	@Query(value = "SELECT e FROM ExamTimeTableEntity e WHERE e.exam.id=:examId")
	List<ExamTimeTableEntity> getExamTimetableByExamId(@Param("examId") long examId);
	
	@Query(value = "SELECT e FROM ExamTimeTableEntity e WHERE e.subject.id=:subjectId")
	List<ExamTimeTableEntity> getExamTimetableBySubjectId(@Param("subjectId") long subjectId);
	
	@Query(value = "SELECT e FROM ExamTimeTableEntity e WHERE e.classes.id=:classId")
	List<ExamTimeTableEntity> getExamTimetableByClassId(@Param("classId") long classId);
	
	@Query(value = "SELECT e FROM ExamTimeTableEntity e WHERE e.school.id=:schoolId and  e.subject.id=:subjectId and e.classes.id=:classId")
	List<ExamTimeTableEntity> getExamTimetableBySubjectAnClassId(@Param("schoolId") long schoolId, @Param("subjectId") long subjectId, @Param("classId") long classId);

}
