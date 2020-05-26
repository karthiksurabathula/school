package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.OutboundEntity;

@Repository
public interface OutboundRepository extends JpaRepository<OutboundEntity, Long> {

	@Query(value = "SELECT s FROM OutboundEntity s WHERE s.school.id=:schoolId")
	List<OutboundEntity> getByBySchoolId(@Param("schoolId") long schoolId);

}
