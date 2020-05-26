package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.SubjectStaffMapEntity;

@Repository
public interface SubjectStaffMapRepository extends JpaRepository<SubjectStaffMapEntity, Long> {

	@Query(value = "SELECT s FROM SubjectStaffMapEntity s WHERE s.school.id=:schoolId and s.subject.id=:subjectId and s.classes.id=:classId and s.section.id=:sectionId")
	SubjectStaffMapEntity getStaffBySubjectSchool(@Param("schoolId") long schoolId, @Param("subjectId") long subjectId, @Param("classId") long classId, @Param("sectionId") long sectionId);

	@Query(value = "SELECT s.staff.id FROM SubjectStaffMapEntity s WHERE s.school.id=:schoolId and s.subject.id=:subjectId")
	Object getStaffIdBySubjectSchool(@Param("schoolId") long schoolId, @Param("subjectId") long subjectId);

	@Query(value = "SELECT s.staff.id FROM SubjectStaffMapEntity s WHERE s.school.id=:schoolId and s.subject.id=:subjectId and s.classes.id=:classId and s.section.id=:sectionId")
	Object getStaffBySubjectClassSectionSchool(@Param("schoolId") long schoolId, @Param("subjectId") long subjectId, @Param("classId") long classId, @Param("sectionId") long sectionId);

	@Query(value = "SELECT s.staff.name FROM SubjectStaffMapEntity s WHERE s.school.id=:schoolId and s.subject.id=:subjectId and s.classes.id=:classId and s.section.id=:sectionId")
	Object getStaffNameBySubjectClassSectionSchool(@Param("schoolId") long schoolId, @Param("subjectId") long subjectId, @Param("classId") long classId, @Param("sectionId") long sectionId);

	// Staff Timetable
	@Query(value = "SELECT p.startTime,p.endTime,c.className,s.sectionName,sb.subjectName,t.calendarDay.day FROM SubjectStaffMapEntity sm " + "left outer join StaffEntity st on sm.staff.id= st.id " + "left outer join ClassEntity c on sm.classes.id=c.id " + "left outer join SectionEntity s on sm.section.id=s.id " + "left outer join TimeTableEntity t on sm.classes.id=t.classes.id and sm.section.id=t.section.id and sm.subject.id=t.subject.id " + "left outer join SubjectEntity sb on t.subject.id = sb.id " + "inner join PeriodEntity p on t.period.id=p.id where st.staffId=:staffId and t.calendarDay.day=:day and sm.school.id=:schoolId ORDER BY p.startTime ASC ")
	List<Object[]> getStaffTimeTable(@Param("schoolId") long schoolId, @Param("staffId") String staffId, @Param("day") String day);

	//

	@Query(value = "SELECT s FROM SubjectStaffMapEntity s WHERE s.staff.id=:staffId")
	List<SubjectStaffMapEntity> getByStaffId(@Param("staffId") long staffId);

	@Query(value = "SELECT s FROM SubjectStaffMapEntity s WHERE s.school.id=:schoolId and  s.subject.id=:subjectId and  s.classes.id=:classId")
	List<SubjectStaffMapEntity> getBySubjectAndClassId(@Param("schoolId") long schoolId, @Param("subjectId") long subjectId, @Param("classId") long classId);

	@Query(value = "SELECT s FROM SubjectStaffMapEntity s WHERE s.section.id=:sectionId")
	List<SubjectStaffMapEntity> getBySectionId(@Param("sectionId") long sectionId);
	
}
