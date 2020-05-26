package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.SubjectClassMapEntity;

@Repository
public interface SubjectClassMapRepository extends JpaRepository<SubjectClassMapEntity, Long> {

	@Query(value = "SELECT s FROM SubjectClassMapEntity s WHERE s.school.id=:schoolId and s.classes.id=:classId and s.subject.id=:subjectId")
	SubjectClassMapEntity getSubjectAndClassMap(@Param("schoolId") long schoolId, @Param("classId") long classId, @Param("subjectId") long subjectId);

	@Query(value = "SELECT s.id,s.optional,sb.subjectName,sb.id FROM SubjectClassMapEntity s INNER JOIN s.subject sb WHERE s.school.id=:schoolId and s.classes.id=:classId")
	List<Object[]> getSubjectsMappedToSchoolClass(@Param("schoolId") long schoolId, @Param("classId") long classId);

	@Query(value = "SELECT s FROM SubjectClassMapEntity s WHERE s.id=:subMapId and s.school.id=:schoolId")
	SubjectClassMapEntity getSubjectAndClassMapById(@Param("subMapId") long subMapId, @Param("schoolId") long schoolId);

	@Query(value = "SELECT s FROM SubjectClassMapEntity s WHERE s.school.id=:schoolId and s.classes.id=:classId")
	List<SubjectClassMapEntity> getSubjectClassMapByClass(@Param("schoolId") long schoolId, @Param("classId") long classId);

	//
	
	@Query(value = "SELECT s FROM SubjectClassMapEntity s WHERE s.school.id=:schoolId")
	List<SubjectClassMapEntity> getSubjectClassMapBySchool(@Param("schoolId") long schoolId);
	
	
	@Query(value = "SELECT s FROM SubjectClassMapEntity s WHERE s.school.id=:schoolId and  s.subject.id=:subjectId")
	List<SubjectClassMapEntity> getSubjectClassMapBySubjectId(@Param("schoolId") long schoolId, @Param("subjectId") long subjectId);
	
	@Query(value = "SELECT s FROM SubjectClassMapEntity s WHERE s.school.id=:schoolId and  s.subject.id=:subjectId and s.classes.id=:classId")
	List<SubjectClassMapEntity> getSubjectClassMapBySubjectAndClassId(@Param("schoolId") long schoolId, @Param("subjectId") long subjectId, @Param("classId") long classId);
	
	@Query(value = "SELECT s FROM SubjectClassMapEntity s WHERE s.classes.id=:classId")
	List<SubjectClassMapEntity> getSubjectClassMapByClassId(@Param("classId") long classId);


}
