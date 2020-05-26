package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.SectionEntity;

@Repository
public interface SectionRepository extends JpaRepository<SectionEntity, Long> {

	@Query(value = "SELECT s FROM SectionEntity s WHERE s.school.id=:schoolId and s.classes.id=:classId ")
	List<SectionEntity> getBySectionsBySchoolAndClass(@Param("schoolId") long schoolId, @Param("classId") long classId);

	@Query(value = "SELECT s FROM SectionEntity s WHERE s.school.id=:schoolId and s.id=:sectionId ")
	SectionEntity getBySectionsBySchoolAndSection(@Param("schoolId") long schoolId, @Param("sectionId") long sectionId);

	@Query(value = "SELECT s FROM SectionEntity s WHERE s.classes.id=:classId ")
	List<SectionEntity> getBySectionsByClassId(@Param("classId") long classId);

}
