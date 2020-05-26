package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.SchoolEntity;

@Repository
public interface SchoolRepository extends JpaRepository<SchoolEntity, Long> {

	@Query(value = "SELECT s FROM SchoolEntity s WHERE s.schoolCity.id=:cityCode")
	List<SchoolEntity> getBySchoolByCityid(@Param("cityCode") long cityCode);

	@Query(value = "SELECT s FROM SchoolEntity s WHERE s.id=:sclId")
	SchoolEntity getBySchoolById(@Param("sclId") long sclId);

}
