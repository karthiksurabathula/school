package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.MarksTrackerEntity;

@Repository
public interface MarksTrackerRepository extends JpaRepository<MarksTrackerEntity, Long> {

	@Query(value = "SELECT m FROM MarksTrackerEntity m WHERE m.school.id=:schoolId and m.student.id=:studentId and m.exam.id=:examId")
	MarksTrackerEntity getMarksByStudent(@Param("schoolId") long schoolId, @Param("studentId") long studentId, @Param("examId") long examId);

	@Query(value = "SELECT m FROM MarksTrackerEntity m WHERE m.school.id=:schoolId and m.student.id IN :studentIds and m.exam.id=:examId  ORDER BY m.marks DESC")
	List<MarksTrackerEntity> getMarksByClassAndExam(@Param("schoolId") long schoolId, @Param("examId") long examId, @Param("studentIds") List<Long> studentIds);
	
	//
	
	@Query(value = "SELECT m FROM MarksTrackerEntity m WHERE m.exam.id=:examId")
	List<MarksTrackerEntity> getMarksByExamId(@Param("examId") long examId);
	
	@Query(value = "SELECT m FROM MarksTrackerEntity m WHERE  m.student.id=:studentIds")
	List<MarksTrackerEntity> getMarksByStudentId(@Param("studentIds") long studentIds);
	
	@Query(value = "SELECT m FROM MarksTrackerEntity m WHERE m.school.id=:schoolId and  m.student.id=:studentId and m.exam.id=:examId")
	MarksTrackerEntity getMarksByStudentAndExamId(@Param("schoolId") long schoolId, @Param("examId") long examId, @Param("studentId") long studentId);
	


}
