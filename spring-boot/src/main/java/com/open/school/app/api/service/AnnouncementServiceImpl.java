package com.open.school.app.api.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.AnnouncementEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.repository.AnnouncementRepository;

@Service
public class AnnouncementServiceImpl {

	@Autowired
	private AnnouncementRepository announcementRepo;
	@Autowired
	private DeleteServiceLogic deletService;
	private static final Logger log = LogManager.getLogger(AnnouncementServiceImpl.class);


	public ResponseEntity<?> getAnnouncements(long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		List<AnnouncementEntity> announcement = null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String role = auth.getAuthorities().iterator().next().getAuthority().toString();
			if (role.equals("SUPERUSER") || role.equals("ADMIN")) {
				announcement = announcementRepo.getAnnouncement(schoolId);
			} else {
				announcement = announcementRepo.getAnnouncementByVisibility(schoolId, true);
			}

			response.put("announcement", announcement);
			response.put("indicator", "success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1001");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> createAnnouncement(long schoolId, AnnouncementEntity announcemnt) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			AnnouncementEntity announcement = announcementRepo.getAnnouncementById(schoolId, announcemnt.getId());
			if (announcement == null) {
				announcemnt.setLastModified(new Date());
				announcemnt.setModifiedBy(auth.getName());
				announcemnt.setVisibilty(true);
				announcemnt.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
				announcementRepo.saveAndFlush(announcemnt);
			} else {
				announcement.setTitle(announcemnt.getTitle());
				announcement.setDescription(announcemnt.getDescription());
				announcement.setLastModified(new Date());
				announcement.setModifiedBy(auth.getName());
				announcementRepo.saveAndFlush(announcement);
			}
			response.put("indicator", "success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1001");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> deleteAnnouncement(long schoolId, long announcemntId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			AnnouncementEntity announcement = announcementRepo.getAnnouncementById(schoolId, announcemntId);
			if (announcement == null) {
				response.put("indicator", "fail");
				response.put("message", "Announcement Record not found");
			} else {
				deletService.deleteAnnouncement(announcement.getId());
//				 announcementRepo.delete(announcement);
				response.put("indicator", "success");
			}

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1001");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> setAnnouncementVisibilty(long schoolId, AnnouncementEntity announcemnt) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			AnnouncementEntity announcement = announcementRepo.getAnnouncementById(schoolId, announcemnt.getId());
			if (announcement == null) {
				response.put("indicator", "fail");
				response.put("message", "Announcement Record not found");
			} else {
				announcement.setVisibilty(announcemnt.isVisibilty());
				announcement.setLastModified(new Date());
				announcement.setModifiedBy(auth.getName());
				announcementRepo.saveAndFlush(announcement);
				response.put("indicator", "success");
			}

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1001");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
