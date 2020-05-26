package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.AttendanceEntity;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {

	@Query(value = "SELECT a FROM AttendanceEntity a WHERE a.school.id=:schoolId and a.classes.id=:classId and a.section.id=:sectionId and a.day.id=:dayId")
	List<AttendanceEntity> getAttendanceByClassAndSection(@Param("schoolId") long schoolId, @Param("classId") long classId, @Param("sectionId") long sectionId, @Param("dayId") long dayId);

	@Query(value = "SELECT a FROM AttendanceEntity a WHERE a.school.id=:schoolId and a.classes.id=:classId and a.section.id=:sectionId and a.day.id=:dayId and a.student.id=:studentId")
	AttendanceEntity getAttendanceByClassAndSectionAndStudent(@Param("schoolId") long schoolId, @Param("classId") long classId, @Param("sectionId") long sectionId, @Param("dayId") long dayId, @Param("studentId") long studentId);

	@Query(value = "SELECT a FROM AttendanceEntity a WHERE a.school.id=:schoolId and a.day.id=:dayId")
	List<AttendanceEntity> getAbsenteesBySchool(@Param("schoolId") long schoolId, @Param("dayId") long dayId);

	@Query(value = "SELECT a FROM AttendanceEntity a WHERE a.school.id=:schoolId and a.student.id=:studentId")
	List<AttendanceEntity> getAttendanceByClassAndSectionAndStudent(@Param("schoolId") long schoolId, @Param("studentId") long studentId);

	@Query(value = "SELECT a FROM AttendanceEntity a WHERE a.student.id=:studentId")
	List<AttendanceEntity> getAttendanceByStudent( @Param("studentId") long studentId);
	
	
	@Query(value = "SELECT a FROM AttendanceEntity a WHERE a.section.id=:sectionId")
	List<AttendanceEntity> getAttendanceBySectionId( @Param("sectionId") long sectionId);
	
	@Query(value = "SELECT Count(a) FROM AttendanceEntity a WHERE a.school.id=:schoolId and a.day.id=:dayId")
	long getAbsenteesBySchoolCount(@Param("schoolId") long schoolId, @Param("dayId") long dayId);



}
