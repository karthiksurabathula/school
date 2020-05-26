package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.PeriodEntity;

@Repository
public interface PeriodRepository extends JpaRepository<PeriodEntity, Long> {

	@Query(value = "SELECT p FROM PeriodEntity p where p.school.id=:schoolId ORDER BY p.startTime ASC")
	List<PeriodEntity> getPeriodsBySchool(@Param("schoolId") long schoolId);

	@Query(value = "SELECT p FROM PeriodEntity p where p.school.id=:schoolId and p.id=:periodId")
	PeriodEntity getPeriodBySchool(@Param("schoolId") long schoolId, @Param("periodId") long periodId);

}
