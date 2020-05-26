package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.StudentEntity;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

	@Query(value = "SELECT s FROM StudentEntity s WHERE s.school.id=:schoolId")
	List<StudentEntity> getByStudentsBySchool(@Param("schoolId") long schoolId);

	@Query(value = "SELECT count(s) FROM StudentEntity s WHERE s.school.id=:schoolId")
	long getCountOfStudentsBySchool(@Param("schoolId") long schoolId);

	@Query(value = "SELECT s FROM StudentEntity s WHERE s.studentId=:studentId and s.school.id=:schoolId")
	StudentEntity getByStudentIdBySchool(@Param("studentId") String studentId, @Param("schoolId") long schoolId);

	@Query(value = "SELECT Count(s) FROM StudentEntity s WHERE s.studentId=:studentId and s.school.id=:schoolId")
	int getCoutOfStudentsIdByStudentIdAndSchool(@Param("studentId") String studentId, @Param("schoolId") long schoolId);

	@Query(value = "SELECT s FROM StudentEntity s WHERE s.id=:stuId and s.school.id=:schoolId")
	StudentEntity getByStudentIdBySchool(@Param("stuId") long stuId, @Param("schoolId") long schoolId);

	@Query(value = "SELECT s FROM StudentEntity s WHERE s.id IN :studentIds and s.school.id=:schoolId")
	List<StudentEntity> getByStudentsByIdsBySchool(@Param("studentIds") List<Long> studentIds, @Param("schoolId") long schoolId);

	@Query(value = "SELECT s FROM StudentEntity s WHERE s.pending= :status and s.school.id=:schoolId")
	List<StudentEntity> getByStudentsPendingBySchool(@Param("status") boolean status, @Param("schoolId") long schoolId);
	
	//Aanalytics
	@Query(value = "SELECT count(s) FROM StudentEntity s WHERE s.school.id=:schoolId")
	int getByStudentsCountBySchool(@Param("schoolId") long schoolId);
	
	@Query(value = "SELECT s FROM StudentEntity s WHERE s.username=:username")
	StudentEntity getByStudentsByUsername(@Param("username") String username);
	
}
