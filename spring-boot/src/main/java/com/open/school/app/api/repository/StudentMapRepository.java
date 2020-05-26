package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.StudentMapEntity;

@Repository
public interface StudentMapRepository extends JpaRepository<StudentMapEntity, Long> {

	@Query(value = "SELECT s FROM StudentMapEntity s WHERE s.student.id=:stuid and s.school.id=:schoolId")
	StudentMapEntity getByStudentId(@Param("stuid") long stuid, @Param("schoolId") long schoolId);

	@Query(value = "SELECT s FROM StudentMapEntity s WHERE s.classes.id=:classId and s.school.id=:schoolId")
	List<StudentMapEntity> getByStudentsByClassId(@Param("classId") long classId, @Param("schoolId") long schoolId);

	@Query(value = "SELECT st.id,st.studentId,st.name FROM StudentMapEntity s INNER JOIN s.student st ON s.student.id = st.id WHERE s.classes.id=:classId and s.school.id=:schoolId and s.section.id=:sectionId ORDER BY st.studentId ASC")
	List<Object[]> getByStudentsDetailsByClass(@Param("schoolId") long schoolId, @Param("classId") long classId, @Param("sectionId") long sectionId);

	@Query(value = "SELECT s FROM StudentMapEntity s WHERE s.student.id=:stuid")
	StudentMapEntity getByStuId(@Param("stuid") long stuid);
	
	@Query(value = "SELECT s FROM StudentMapEntity s WHERE s.section.id=:sectionId")
	List<StudentMapEntity> getBySectionId(@Param("sectionId") long sectionId);

}
