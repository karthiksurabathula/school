package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.MarksStatusTrackerEntity;

@Repository
public interface MarksStatusTrackerRepository extends JpaRepository<MarksStatusTrackerEntity, Long> {

	@Query(value = "SELECT m FROM MarksStatusTrackerEntity m WHERE m.school.id=:schoolId and m.exam.id=:examId")
	List<MarksStatusTrackerEntity> getSubjectMarksStatusBySchoolAndExam(@Param("schoolId") long schoolId, @Param("examId") long examId);

	@Query(value = "SELECT m FROM MarksStatusTrackerEntity m WHERE m.school.id=:schoolId and m.classes.id=:classId and m.section.id=:sectionId and m.subject.id=:subjectId and m.exam.id=:examId")
	MarksStatusTrackerEntity getSubjectMarksStatus(@Param("schoolId") long schoolId, @Param("classId") long classId, @Param("sectionId") long sectionId, @Param("subjectId") long subjectId, @Param("examId") long examId);

	@Query(value = "SELECT m FROM MarksStatusTrackerEntity m WHERE m.exam.id=:examId")
	List<MarksStatusTrackerEntity> getSubjectMarksStatusByExamId(@Param("examId") long examId);

	@Query(value = "SELECT m FROM MarksStatusTrackerEntity m WHERE m.school.id=:schoolId and  m.subject.id=:subjectId  and m.classes.id=:classId and m.exam.id=:examId")
	List<MarksStatusTrackerEntity> getSubjectMarksStatusBySubjectAndClassAndExamId(@Param("schoolId") long schoolId, @Param("subjectId") long subjectId,  @Param("classId") long classId, @Param("examId") long examId);
	
	@Query(value = "SELECT m FROM MarksStatusTrackerEntity m WHERE m.section.id=:sectionId")
	List<MarksStatusTrackerEntity> getSubjectMarksStatusBySectionId( @Param("sectionId") long sectionId);

}
