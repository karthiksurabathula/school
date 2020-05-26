package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.ExamEntity;

@Repository
public interface ExamRepository extends JpaRepository<ExamEntity, Long> {

	@Query(value = "SELECT e FROM ExamEntity e WHERE e.id=:examId and e.school.id=:schoolId")
	ExamEntity getExamByIdAndSchool(@Param("examId") long examId, @Param("schoolId") long schoolId);

	@Query(value = "SELECT e FROM ExamEntity e WHERE e.school.id=:schoolId")
	List<ExamEntity> getExamsBySchool(@Param("schoolId") long schoolId);

	@Query(value = "SELECT e FROM ExamEntity e WHERE (e.school.id=:schoolId and e.scope='School') or ( e.classes.id=:classId and e.scope='Class') or ( e.section.id=:sectionId and e.scope='Section')")
	List<ExamEntity> getExamsBySchoolAndClassAndSection(@Param("schoolId") long schoolId, @Param("classId") long classId, @Param("sectionId") long sectionId);

	
	@Query(value = "SELECT e FROM ExamEntity e WHERE e.school.id=:schoolId and e.scope=:scope")
	List<ExamEntity> getExamsBySchoolAndScope(@Param("schoolId") long schoolId, @Param("scope") String scope);
	
	@Query(value = "SELECT e FROM ExamEntity e WHERE e.section.id=:sectionId")
	List<ExamEntity> getExamsBySectionId(@Param("sectionId") long sectionId);
	
	@Query(value = "SELECT e FROM ExamEntity e WHERE e.classes.id=:classId")
	List<ExamEntity> getExamsByClassId(@Param("classId") long classId);

	@Query(value = "SELECT e FROM ExamEntity e WHERE e.section.id=:sectionId and e.scope=:scope")
	List<ExamEntity> getExamsBySectionAndScope(@Param("sectionId") long sectionId, @Param("scope") String scope);
	
	
	@Query(value = "SELECT e FROM ExamEntity e WHERE e.completed=:completed")
	List<ExamEntity> getExamsByCompletionStatus(@Param("completed") boolean completed);
	
	@Query(value = "SELECT count(e) FROM ExamEntity e WHERE e.completed=:completed and e.school.id=:schoolId ")
	long getExamsByCompletionStatusAndSchool(@Param("completed") boolean completed, @Param("schoolId") long schoolId);
	
	@Query(value = "SELECT count(e) FROM ExamEntity e WHERE ((e.school.id=:schoolId and e.scope='School') or ( e.classes.id=:classId and e.scope='Class') or ( e.section.id=:sectionId and e.scope='Section')) and e.completed=:completed ")
	long getExamsCountBySchoolAndClassAndSection(@Param("schoolId") long schoolId, @Param("classId") long classId, @Param("sectionId") long sectionId,@Param("completed") boolean completed);

	@Query(value = "SELECT count(e) FROM ExamEntity e WHERE ((e.school.id=:schoolId and e.scope='School') or ( e.classes.id In (Select sf.classes.id from SubjectStaffMapEntity sf where sf.staff.id=:staffId) and e.scope='Class') or ( e.section.id In (select sfe.section.id from SubjectStaffMapEntity sfe where sfe.staff.id=:staffId) and e.scope='Section')) and e.completed=:completed ")
	long getUpcommingtExamsStaff(@Param("schoolId") long schoolId,@Param("completed") boolean completed, @Param("staffId") long staffId);

}
