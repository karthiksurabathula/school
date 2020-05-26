package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.TimeTableEntity;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTableEntity, Long> {

	@Query(value = "SELECT t FROM TimeTableEntity t WHERE t.school.id=:schoolId and t.classes.id=:classId and t.section.id=:sectionId and t.calendarDay.day=:day and t.period.id=:periodId")
	TimeTableEntity getTimeTableBySectionAndDay(@Param("schoolId") long schoolId, @Param("classId") long classId, @Param("sectionId") long sectionId, @Param("day") String day, @Param("periodId") long periodId);

	//
	
	@Query(value = "SELECT t FROM TimeTableEntity t WHERE t.school.id=:schoolId and  t.subject.id=:subjectId and t.classes.id=:classId ")
	List<TimeTableEntity> getTimeTableBySubjectAndClassId(@Param("schoolId") long schoolId, @Param("subjectId") long subjectId, @Param("classId") long classId);
	
	@Query(value = "SELECT t FROM TimeTableEntity t WHERE t.school.id=:schoolId and   t.period.id=:periodId")
	List<TimeTableEntity> getTimeTableByPeriodId(@Param("schoolId") long schoolId,  @Param("periodId") long periodId);
	
	@Query(value = "SELECT t FROM TimeTableEntity t WHERE t.section.id=:sectionId")
	List<TimeTableEntity> getTimeTableBySectionId( @Param("sectionId") long sectionId);

}
