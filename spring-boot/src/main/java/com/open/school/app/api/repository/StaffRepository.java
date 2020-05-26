package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.StaffEntity;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, Long> {

	@Query(value = "SELECT s FROM StaffEntity s WHERE s.school.id=:schoolId")
	List<StaffEntity> getByStaffBySchool(@Param("schoolId") long schoolId);

	@Query(value = "SELECT s FROM StaffEntity s WHERE s.school.id=:schoolId and s.id= :staffId")
	StaffEntity getByStaffBySchoolAndId(@Param("schoolId") long schoolId, @Param("staffId") long staffId);

	@Query(value = "SELECT count(s) FROM StaffEntity s WHERE s.school.id=:schoolId")
	long getByStaffCountBySchoolAndId(@Param("schoolId") long schoolId);

	@Query(value = "SELECT s FROM StaffEntity s WHERE s.school.id=:schoolId and s.staffId= :staffId")
	StaffEntity getByStaffBySchoolAndIdString(@Param("schoolId") long schoolId, @Param("staffId") String staffId);

}
