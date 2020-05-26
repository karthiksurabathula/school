package com.open.school.app.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.jwt.entity.ApiRequestEntity;

@Repository
public interface ApiRequestRepository extends JpaRepository<ApiRequestEntity, Long> {

	@Query(value = "SELECT a FROM ApiRequestEntity a WHERE a.request=:request and a.type=:type ")
	ApiRequestEntity findApiRequest(@Param("request") String apiId, @Param("type") String type);

}
