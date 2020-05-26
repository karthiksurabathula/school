package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.AttendenceTrackerEntity;

@Repository
public interface AttendenceTrackerRepository extends JpaRepository<AttendenceTrackerEntity, Long>{

	@Query(value = "SELECT a FROM AttendenceTrackerEntity a WHERE a.school.id=:schoolId and a.classes.id=:classId and a.section.id=:sectionId and a.day.id=:dayId")
	AttendenceTrackerEntity getAttendanceTrackerByClassAndSection(@Param("schoolId") long schoolId, @Param("classId") long classId, @Param("sectionId") long sectionId, @Param("dayId") long dayId);

	@Query(value = "SELECT a FROM AttendenceTrackerEntity a WHERE a.school.id=:schoolId and a.day.id=:dayId")
	List<AttendenceTrackerEntity> getAttendanceTrackerBySchoolAndDay(@Param("schoolId") long schoolId, @Param("dayId") long dayId);
	
	@Query(value = "SELECT a FROM AttendenceTrackerEntity a WHERE a.section.id=:sectionId")
	List<AttendenceTrackerEntity> getAttendanceTrackerBySectionId(@Param("sectionId") long sectionId);

}