package com.open.school.app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.AnnouncementEntity;

@Repository
public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long>{
	
	@Query(value = "SELECT a FROM AnnouncementEntity a WHERE a.school.id=:schoolId")
	List<AnnouncementEntity> getAnnouncement(@Param("schoolId") long schoolId);
	
	@Query(value = "SELECT a FROM AnnouncementEntity a WHERE a.school.id=:schoolId and a.visibilty=:visibilty")
	List<AnnouncementEntity> getAnnouncementByVisibility(@Param("schoolId") long schoolId, @Param("visibilty") boolean visibilty);

	@Query(value = "SELECT a FROM AnnouncementEntity a WHERE a.school.id=:schoolId and a.id=:announcementId")
	AnnouncementEntity getAnnouncementById(@Param("schoolId") long schoolId,@Param("announcementId") long announcementId);
	
	@Query(value = "SELECT count(a) FROM AnnouncementEntity a WHERE a.school.id=:schoolId and a.lastModified < CURRENT_DATE")
	long getCurrentAnnouncementCount(@Param("schoolId") long schoolId);



}
