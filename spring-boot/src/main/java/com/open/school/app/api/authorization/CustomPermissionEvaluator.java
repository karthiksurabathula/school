package com.open.school.app.api.authorization;

import java.io.BufferedReader;
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.open.school.app.jwt.entity.ApiRequestEntity;
import com.open.school.app.jwt.entity.EntitlementByRoleEntity;
import com.open.school.app.jwt.entity.IncidentEntity;
import com.open.school.app.jwt.entity.UserOrgEntity;
import com.open.school.app.jwt.repository.ApiRequestRepository;
import com.open.school.app.jwt.repository.EntitlementByRoleRepository;
import com.open.school.app.jwt.repository.IncidentRepository;
import com.open.school.app.jwt.repository.UserOrgRepository;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

	@Autowired
	private ApiRequestRepository apiRepository;
	@Autowired
	private EntitlementByRoleRepository entitlementByRoleRepository;
	@Autowired
	private UserOrgRepository userOrgRepository;
	@Autowired
	private IncidentRepository incidentRepo;
	private HttpServletRequest request;

	@Value("${url.login}")
	private String loginurl;

	@Autowired
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	// Dummy Not to be used for now
	@Override
	public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
		return false;
	}

	@Override
	public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
		if ((auth == null) || !(targetDomainObject instanceof String) || !(permission instanceof String)) {
			return false;
		}

		return hasPrivilege(auth, targetDomainObject.toString().toUpperCase(), permission.toString().toUpperCase());
	}

	private boolean hasPrivilege(Authentication auth, String request, String typeOfRequest) {
		// Get API Request and check Entitlement
		ApiRequestEntity api = apiRepository.findApiRequest(request, typeOfRequest);
		EntitlementByRoleEntity entitlement = entitlementByRoleRepository.findEntielementByApiAndRole(api.getId(), auth.getAuthorities().iterator().next().toString());
		if (entitlement == null) {
			String message = "User tried to access Request: " + request + ", Type: " + typeOfRequest + ", User Authority: " + auth.getAuthorities().iterator().next().toString();
			incidentRepo.saveAndFlush(new IncidentEntity(0, clientIp(), auth.getName(), message, getRequestDetails(), null, new Date(), "Security", "CustomPermissionEvaluator"));
			return false;
		} else {
			return entitlement.isAccess();
		}

	}

	public ResponseEntity<?> hasPrivilege(long schoolId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!auth.getAuthorities().iterator().next().toString().equals("SUPERUSER")) {
			UserOrgEntity userOrgMap = userOrgRepository.findUserOrgMap(auth.getName(), schoolId);
			if (userOrgMap == null) {
				String message = "User tried to access Details from school id:\"" + schoolId + "\" but the user is not assigned to school";
				incidentRepo.saveAndFlush(new IncidentEntity(0, clientIp(), auth.getName(), message, getRequestDetails(), null, new Date(), "Security", "CustomPermissionEvaluator"));
				HashMap<String, Object> response = new HashMap<>();
				response.put("status", 401);
				response.put("message", "Unauthorized");
				response.put("redirecturl", loginurl);
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			}
		}
		return null;
	}

	public String getRequestDetails() {
		try {
			StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
			String queryString = request.getQueryString();
			StringBuffer requestBody = new StringBuffer();
			String line = null;
			String newLine = System.lineSeparator();
			String headers = "", headerName;
			Enumeration<String> headerNames = request.getHeaderNames();

			if (headerNames != null) {
				while (headerNames.hasMoreElements()) {
					headerName = headerNames.nextElement();
					headers = headers + headerName + ":" + request.getHeader(headerName) + newLine;
				}
			}

			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				requestBody.append(line);

			return newLine + request.getMethod() + newLine + headers + requestURL + "?" + queryString + newLine + requestBody;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String clientIp() {
		String remoteAddr = "";
		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
			}
		}
		return remoteAddr;
	}

}
