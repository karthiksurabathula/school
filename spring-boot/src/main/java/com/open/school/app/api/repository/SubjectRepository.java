package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.SubjectEntity;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {

	@Query(value = "SELECT s FROM SubjectEntity s WHERE s.school.id=:schoolId and s.id=:subjectId")
	SubjectEntity getSubjectBySchool(@Param("schoolId") long schoolId, @Param("subjectId") long subjectId);

	@Query(value = "SELECT s FROM SubjectEntity s WHERE s.school.id=:schoolId")
	List<SubjectEntity> getSubjectsBySchool(@Param("schoolId") long schoolId);

}
