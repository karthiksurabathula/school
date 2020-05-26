package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.ClassEntity;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {

	@Query(value = "SELECT c FROM ClassEntity c WHERE c.school.id=:schoolId")
	List<ClassEntity> getClassessBySchool(@Param("schoolId") long schoolId);

	@Query(value = "SELECT c FROM ClassEntity c WHERE c.school.id=:schoolId and c.id=:classId")
	ClassEntity getClassBySchoolAndClassId(@Param("schoolId") long schoolId, @Param("classId") long classId);

}
