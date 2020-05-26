package com.open.school.app.api.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.InitializationTrackerEntity;
import com.open.school.app.api.entity.PeriodEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.model.period.PeriodReqModel;
import com.open.school.app.api.repository.InitializationTrackerRepository;
import com.open.school.app.api.repository.PeriodRepository;

@Service
public class PeriodServiceImpl {

	@Autowired
	private PeriodRepository periodRepository;
	@Autowired
	private InitializationTrackerRepository initiRepo;
	@Autowired
	private DeleteServiceLogic deleteService;

	DateFormat format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
	DateFormat format1 = new SimpleDateFormat("hh:mm", Locale.ENGLISH);

	private static final Logger log = LogManager.getLogger(PeriodServiceImpl.class);

	public ResponseEntity<?> savePeriod(PeriodReqModel period, long schoolId) {

		HashMap<String, Object> response = new HashMap<>();
		String message = "";
		Date target;
		PeriodEntity pck1 = null, pck2 = null;
		boolean check1 = false, check2 = false;

		try {
			List<PeriodEntity> periods = periodRepository.getPeriodsBySchool(schoolId);

			if (periods.size() > 0) {

				// Start Time Check
				target = format1.parse(period.getStartTime());
				for (int i = 0; i < periods.size(); i++) {
					if (periods.get(i).getId() != period.getId()) {
						Date start = DateUtils.addMinutes(periods.get(i).getStartTime(), +1);
						Date end = DateUtils.addMinutes(periods.get(i).getEndTime(), -1);
						check1 = (target.after(start) && target.before(end));
						if (check1) {
							pck1 = periods.get(i);
							break;
						}
					} else {
						check1 = false;
					}
				}

				// End Time Check
				target = format1.parse(period.getEndTime());
				for (int i = 0; i < periods.size(); i++) {
					if (periods.get(i).getId() != period.getId()) {
						Date start = DateUtils.addMinutes(periods.get(i).getStartTime(), +1);
						Date end = DateUtils.addMinutes(periods.get(i).getEndTime(), -1);
						check2 = (target.after(start) && target.before(end));
						if (check2) {
							pck2 = periods.get(i);
							break;
						}
					} else {
						check2 = false;
					}
				}

			} else {
				check1 = false;
				check2 = false;
			}

			if (check1 || check2) {
				if (check1) {
					response.put("indicator", "fail");
					message = message + "Start time falls in " + pck1.getDescription();
				}
				if (check2) {
					response.put("indicator", "fail");
					if (message != "") {
						message = message + " & End time falls in " + pck2.getDescription();
					} else {
						message = message + " End time falls in " + pck2.getDescription();
					}
				}
				response.put("message", message);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				PeriodEntity newPeriod;
				if (period.getId() != 0) {
					newPeriod = periodRepository.getPeriodBySchool(schoolId, period.getId());
				} else {
					newPeriod = new PeriodEntity();
				}

				newPeriod.setStartTime(format1.parse(period.getStartTime()));
				newPeriod.setEndTime(format1.parse(period.getEndTime()));
				newPeriod.setDescription(period.getDescription());
				newPeriod.setLastModified(new Date());
				newPeriod.setModifiedBy(auth.getName());
				newPeriod.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
				response.put("city", periodRepository.saveAndFlush(newPeriod));
				response.put("indicator", "success");
				response.put("message", period.getDescription() + " Created successfully");

				InitializationTrackerEntity initTracker = initiRepo.getMarksBySclStuExamSubj(schoolId, "period");
				initTracker.setStatus(true);
				initiRepo.saveAndFlush(initTracker);

				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("code", "U1002");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> getPeriodBySchool(long schoolId) {

		HashMap<String, Object> response = new HashMap<>();
		List<Object> periodReqModel = new ArrayList<Object>();
		String message = null;
		try {
			List<PeriodEntity> periods = periodRepository.getPeriodsBySchool(schoolId);
			if (periods.size() > 0) {
				response.put("indicator", "success");
				for (int i = 0; i < periods.size(); i++) {
					PeriodReqModel periodResp = new PeriodReqModel();
					periodResp.setId(periods.get(i).getId());
					periodResp.setDescription(periods.get(i).getDescription());
					periodResp.setStartTime(format1.format(periods.get(i).getStartTime()));
					periodResp.setStartTimeDisp(format.format(periods.get(i).getStartTime()));
					periodResp.setEndTime(format1.format(periods.get(i).getEndTime()));
					periodResp.setEndTimeDisp(format.format(periods.get(i).getEndTime()));
					periodReqModel.add(periodResp);
				}
				response.put("period", periodReqModel);
			} else {
				response.put("indicator", "fail");
				response.put("message", "No Periods Found");
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

	public ResponseEntity<?> deletePeriod(long schoolId, long periodId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			PeriodEntity period = periodRepository.getPeriodBySchool(schoolId, periodId);
			if (period == null) {
				response.put("indicator", "fail");
				response.put("message", "No Periods Found");
			} else {
				deleteService.deletePeriod(schoolId, periodId);
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
