package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.InitializationTrackerEntity;

@Repository
public interface InitializationTrackerRepository extends JpaRepository<InitializationTrackerEntity, Long>  {

	@Query(value = "SELECT i FROM InitializationTrackerEntity i WHERE i.school.id=:schoolId and i.identifier=:identifier")
	InitializationTrackerEntity getMarksBySclStuExamSubj(@Param("schoolId") long schoolId, @Param("identifier") String identifier);

	@Query(value = "SELECT i FROM InitializationTrackerEntity i WHERE i.school.id=:schoolId")
	List<InitializationTrackerEntity> getInitBySchool(@Param("schoolId") long schoolId);
	
	@Query(value = "SELECT i FROM InitializationTrackerEntity i WHERE i.identifier=:identifier")
	InitializationTrackerEntity getMarksBySclStuExamSubj(@Param("identifier") String identifier);


}
