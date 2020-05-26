package com.open.school.app.jwt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.jwt.entity.UserOrgEntity;

@Repository
public interface UserOrgRepository extends JpaRepository<UserOrgEntity, String> {

	@Query(value = "SELECT u FROM UserOrgEntity u WHERE u.loginUser.username =:username and u.school.id =:schoolId ")
	UserOrgEntity findUserOrgMap(@Param("username") String username, @Param("schoolId") long schoolId);

	@Query(value = "SELECT u FROM UserOrgEntity u WHERE u.loginUser.username =:username")
	UserOrgEntity findUserOrgMapByUsername(@Param("username") String username);
	
	@Query(value = "SELECT u FROM UserOrgEntity u WHERE u.school.id =:schoolId ")
	List<UserOrgEntity> findUserBySchool( @Param("schoolId") long schoolId);

}
