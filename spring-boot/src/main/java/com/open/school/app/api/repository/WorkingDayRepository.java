package com.open.school.app.api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.open.school.app.api.entity.WorkingDayEntity;

public interface WorkingDayRepository extends JpaRepository<WorkingDayEntity, Long>{
	
	@Query(value = "SELECT w FROM WorkingDayEntity w WHERE w.school.id=:schoolId and w.date=:searchdate")
	WorkingDayEntity getWorkingDayByDate(@Param("schoolId") long schoolId, @Param("searchdate") Date searchdate);

	@Query(value = "SELECT w FROM WorkingDayEntity w WHERE w.school.id=:schoolId")
	List<WorkingDayEntity> getWorkingDayBySchool(@Param("schoolId") long schoolId);

}
