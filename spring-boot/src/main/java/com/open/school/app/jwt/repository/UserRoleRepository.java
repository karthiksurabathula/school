package com.open.school.app.jwt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.open.school.app.jwt.entity.UserRoleEntity;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

	@Query(value = "SELECT r FROM UserRoleEntity r WHERE r.role<>'SUPERUSER' and r.role<>'STUDENT' ")
	List<UserRoleEntity> getUserRoles();
}
