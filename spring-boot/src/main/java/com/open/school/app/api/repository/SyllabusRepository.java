package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.SyllabusEntity;

@Repository
public interface SyllabusRepository extends JpaRepository<SyllabusEntity, Long>  {

	@Query(value = "SELECT s FROM SyllabusEntity s WHERE s.school.id=:schoolId and s.id=:syllabusId")
	SyllabusEntity getSyllabus(@Param("schoolId") long schoolId, @Param("syllabusId") long syllabusId);
	
	@Query(value = "SELECT s FROM SyllabusEntity s WHERE s.school.id=:schoolId and s.classes.id=:classId and s.section.id=:sectionId and s.subject.id=:subjectId")
	SyllabusEntity getSyllabusBySection(@Param("schoolId") long schoolId, @Param("classId") long classId,@Param("sectionId") long sectionId, @Param("subjectId") long subjectId);

	@Query(value = "SELECT s FROM SyllabusEntity s WHERE s.subject.id=:subjectId")
	SyllabusEntity getSyllabusBySubjectId( @Param("subjectId") long subjectId);
	
	@Query(value = "SELECT s FROM SyllabusEntity s WHERE s.section.id=:sectionId")
	List<SyllabusEntity> getSyllabusBySectionId(@Param("sectionId") long sectionId);


}
