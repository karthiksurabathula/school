package com.open.school.app.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.jwt.entity.EntitlementByRoleEntity;

@Repository
public interface EntitlementByRoleRepository extends JpaRepository<EntitlementByRoleEntity, Long> {

	@Query(value = "SELECT e FROM EntitlementByRoleEntity e WHERE e.api.id =:apiId and e.role.role =:role ")
	EntitlementByRoleEntity findEntielementByApiAndRole(@Param("apiId") long apiId, @Param("role") String role);

}
