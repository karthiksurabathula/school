package com.open.school.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.open.school.app.api.authorization.CustomPermissionEvaluator;
import com.open.school.app.api.entity.AnnouncementEntity;
import com.open.school.app.api.service.AnnouncementServiceImpl;

@RestController
@RequestMapping("api/")
public class AnnouncementController {

	@Autowired
	private CustomPermissionEvaluator customPrevEval;
	@Autowired
	private AnnouncementServiceImpl announcementService;

	@RequestMapping(value = "announcement", method = RequestMethod.GET)
	public ResponseEntity<?> getAnnouncemtnts(@RequestParam long schoolId) {
		return announcementService.getAnnouncements(schoolId);
	}
	
	@PreAuthorize("hasPermission('announcement', 'POST')")
	@RequestMapping(value = "announcement", method = RequestMethod.POST)
	public ResponseEntity<?> postAnnouncements(@RequestBody AnnouncementEntity announcemnt, @RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return announcementService.createAnnouncement(schoolId, announcemnt);
	}
	
	@PreAuthorize("hasPermission('announcementVisibility', 'PUT')")
	@RequestMapping(value = "announcementVisibility", method = RequestMethod.PUT)
	public ResponseEntity<?> setAnnouncementVisibility(@RequestBody AnnouncementEntity announcemnt, @RequestParam long schoolId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return announcementService.setAnnouncementVisibilty(schoolId, announcemnt);
	}
	
	@PreAuthorize("hasPermission('announcement', 'DELETE')")
	@RequestMapping(value = "announcement", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAnnouncements(@RequestParam long schoolId,@RequestParam long announcemntId) {
		ResponseEntity<?> resp = customPrevEval.hasPrivilege(schoolId);
		if (resp != null)
			return resp;
		else
			return announcementService.deleteAnnouncement(schoolId, announcemntId);
	}

}
