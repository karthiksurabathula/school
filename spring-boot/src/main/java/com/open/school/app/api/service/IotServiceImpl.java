package com.open.school.app.api.service;

import java.time.Duration;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.open.school.app.api.model.iot.ApplianceModel;
import com.open.school.app.api.model.iot.IotResponseModel;

@Service
public class IotServiceImpl {

	@Value("${iot.url}")
	private String url;
	@Value("${iot.connectionTimeout}")
	private int connectionTimeout;
	@Value("${iot.readTimeout}")
	private int readTimeout;

	private static final Logger log = LogManager.getLogger(IotServiceImpl.class);

	/**
	 * For Rest template reference
	 * https://attacomsian.com/blog/spring-boot-resttemplate-post-request-json-headers
	 */
	public ResponseEntity<?> createDevices(ApplianceModel appliance, long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			RestTemplate restTemplate = new RestTemplateBuilder().setConnectTimeout(Duration.ofMillis(connectionTimeout)).setReadTimeout(Duration.ofMillis(readTimeout)).build();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			appliance.setUsername(auth.getName());
			appliance.setSchoolId(schoolId);
			ResponseEntity<IotResponseModel> iotResponse = restTemplate.postForEntity(url + "api/appliance?schoolId=" + schoolId, appliance, IotResponseModel.class);
			if (iotResponse.getStatusCode() == HttpStatus.OK) {
				return iotResponse;
			} else {
				response.put("indicator", "fail");
				response.put("message", "Device creation failed");
				log.error("Iot create device failed, response status code" + iotResponse.getStatusCode());
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
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

	public ResponseEntity<?> getDevices(long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			RestTemplate restTemplate = new RestTemplateBuilder().setConnectTimeout(Duration.ofMillis(connectionTimeout)).setReadTimeout(Duration.ofMillis(readTimeout)).build();
			ResponseEntity<Object> iotResponse = restTemplate.getForEntity(url + "api/appliance?schoolId=" + schoolId, Object.class);
			if (iotResponse.getStatusCode() == HttpStatus.OK) {
				return iotResponse;
			} else {
				response.put("indicator", "fail");
				response.put("message", "Failed to retrive devices");
				log.error("Iot create device failed, response status code" + iotResponse.getStatusCode());
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
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
