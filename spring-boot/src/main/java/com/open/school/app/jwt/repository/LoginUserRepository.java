package com.open.school.app.jwt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.jwt.entity.LoginUser;

@Repository
public interface LoginUserRepository extends JpaRepository<LoginUser, String> {

	@Query(value = "SELECT user FROM LoginUser user WHERE user.username=:username ")
	LoginUser findByUsername(@Param("username") String username);
	
	@Query(value = "SELECT user FROM LoginUser user WHERE user.username=:username and  user.school.id=:schoolId")
	LoginUser findByUsernameBySchool(@Param("username") String username,@Param("schoolId") long schoolId);


	@Query(value = "SELECT user FROM LoginUser user WHERE user.restetToken=:restetToken ")
	LoginUser findByToken(@Param("restetToken") String restetToken);

	@Query(value = "SELECT count(*) FROM LoginUser user WHERE user.role.role=:role ")
	long findUsersByAdminRole(@Param("role") String role);
	
	@Query(value = "SELECT user FROM LoginUser user WHERE user.school.id=:schoolId ")
	List<LoginUser> findUserBySchool(@Param("schoolId") long schoolId);

	@Query(value = "SELECT user FROM LoginUser user WHERE user.username=:username ")
	LoginUser findFailureCount(@Param("username") String username);

}
