package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.MarksEntity;

@Repository
public interface MarksRepository extends JpaRepository<MarksEntity, Long> {

	@Query(value = "SELECT m FROM MarksEntity m WHERE m.school.id=:schoolId and m.subject.id=:subjectId and m.exam.id=:examId")
	List<MarksEntity> getMarksByExamAndSubject(@Param("schoolId") long schoolId, @Param("subjectId") long subjectId, @Param("examId") long examId);

	@Query(value = "SELECT m FROM MarksEntity m WHERE m.school.id=:schoolId and m.student.id=:studentId and m.exam.id=:examId")
	List<MarksEntity> getMarksByExamAndStudent(@Param("schoolId") long schoolId, @Param("studentId") long studentId, @Param("examId") long examId);

	@Query(value = "SELECT m FROM MarksEntity m WHERE m.school.id=:schoolId and m.student.id=:studentId and m.exam.id=:examId and m.subject.id=:subjectId")
	MarksEntity getMarksBySclStuExamSubj(@Param("schoolId") long schoolId, @Param("studentId") long studentId, @Param("examId") long examId, @Param("subjectId") long subjectId);

	@Query(value = "SELECT m FROM MarksEntity m WHERE m.exam.id=:examId")
	List<MarksEntity> getMarksByExamId(@Param("examId") long examId);
	
	@Query(value = "SELECT m FROM MarksEntity m WHERE m.student.id=:studentId")
	List<MarksEntity> getMarksByStudentId(@Param("studentId") long studentId);
	
	@Query(value = "SELECT m FROM MarksEntity m WHERE m.subject.id=:subjectId")
	List<MarksEntity> getMarksBySubjectId(@Param("subjectId") long subjectId);
	
	@Query(value = "SELECT m FROM MarksEntity m WHERE  m.school.id=:schoolId and m.subject.id=:subjectId and m.classes.id=:classId")
	List<MarksEntity> getMarksBySubjectAndClassId(@Param("schoolId") long schoolId,  @Param("subjectId") long subjectId,@Param("classId") long classId);


}
