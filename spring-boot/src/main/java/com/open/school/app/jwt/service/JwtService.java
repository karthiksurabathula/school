package com.open.school.app.jwt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.security.auth.login.AccountExpiredException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.open.school.app.api.entity.InitializationTrackerEntity;
import com.open.school.app.api.entity.OutboundEntity;
import com.open.school.app.api.entity.SchoolEntity;
import com.open.school.app.api.entity.StudentEntity;
import com.open.school.app.api.entity.StudentMapEntity;
import com.open.school.app.api.repository.InitializationTrackerRepository;
import com.open.school.app.api.repository.OutboundRepository;
import com.open.school.app.api.repository.StudentMapRepository;
import com.open.school.app.api.repository.StudentRepository;
import com.open.school.app.jwt.config.CustomUserDetails;
import com.open.school.app.jwt.config.JwtTokenUtil;
import com.open.school.app.jwt.entity.LoginUser;
import com.open.school.app.jwt.entity.UserOrgEntity;
import com.open.school.app.jwt.entity.UserRoleEntity;
import com.open.school.app.jwt.model.JwtRequest;
import com.open.school.app.jwt.model.ResetPasswordRequest;
import com.open.school.app.jwt.model.UserSettingRequest;
import com.open.school.app.jwt.repository.LoginUserRepository;
import com.open.school.app.jwt.repository.UserOrgRepository;

@Service
public class JwtService {

	@Autowired
	private LoginUserRepository loginUser;
	@Autowired
	private UserOrgRepository userOrgRepository;
	@Autowired
	private PasswordEncoder bcryptEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private OutboundRepository ouboundRepo;
	@Autowired
	private StudentRepository studentRepo;
	@Autowired
	private StudentMapRepository studentMapRepository;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private InitializationTrackerRepository initiRepo;
	
	@Value("${url.home}")
	private String home;
	@Value("${url.login}")
	private String login;
	@Value("${url.resetPassword}")
	private String restPassword;
	@Value("${url.sampleData}")
	private String sampleData;
	
	

	private static final Logger log = LogManager.getLogger(JwtService.class);


	public ResponseEntity<?> save(JwtRequest user, HttpServletResponse responseMap) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			final LoginUser user1 = loginUser.findByUsername(user.getUsername());
			if (user1 == null) {
				createUser(user.getUsername(), user.getPassword(), "ADMIN", false, 0);
				response.put("indicator", "success");
				response.put("message", "User created successfully");
				response.put("redirecturl", login);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("indicator", "success");
				response.put("redirecturl", login);
				response.put("message", "User already exists");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	public ResponseEntity<?> resetPassword(ResetPasswordRequest passwordReq, HttpServletResponse responseMap) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			final LoginUser user1 = loginUser.findByToken(passwordReq.getRestetToken());
			if (user1 != null) {
				if (passwordReq.getPassword().equals(passwordReq.getPassword1())) {
					user1.setPassword(bcryptEncoder.encode(passwordReq.getPassword()));
					user1.setResetPassword(false);
					loginUser.saveAndFlush(user1);
					response.put("redirecturl", login);
					response.put("indicator", "success");
					response.put("message", "Password updated successfully");
					return new ResponseEntity<>(response, HttpStatus.OK);
					// return authorize(new JwtRequest(user1.getUsername(),
					// passwordReq.getPassword1()), responseMap);
				} else {
					response.put("indicator", "fail");
					response.put("message", "Password mismatch");
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
			} else {
				response.put("indicator", "fail");
				response.put("redirecturl", login);
				response.put("message", "Error Occured, if issue persists please contact administrator");
				log.info("Token Not Found");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	public ResponseEntity<?> resetPasswordLoggedIn(ResetPasswordRequest passwordReq, HttpServletResponse responseMap) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			authenticate(auth.getName(), passwordReq.getCurrentPassword());
			final LoginUser user1 = loginUser.findByUsername(auth.getName());
			if (user1 != null) {
				user1.setPassword(bcryptEncoder.encode(passwordReq.getPassword()));
				user1.setResetPassword(false);
				loginUser.saveAndFlush(user1);
				response.put("indicator", "success");
				response.put("message", "Password updated successfully");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("indicator", "fail");
				response.put("redirecturl", login);
				response.put("message", "Error Occured, if issue persists please contact administrator");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	public ResponseEntity<?> authorize(JwtRequest authenticationRequest, HttpServletResponse responseMap) {
		final String token;
		String message = null;

		HashMap<String, Object> response = new HashMap<>();
		StudentMapEntity studentMap;

		try {
			authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
			final LoginUser user = loginUser.findByUsername(authenticationRequest.getUsername());
			final UserDetails userDetails = new CustomUserDetails(user);

			if (user.isResetPassword()) {
				token = UUID.randomUUID().toString();
				response.put("indicator", "success");
				// response.put("token", token);
				response.put("redirecturl", restPassword + "/" + token);
				user.setRestetToken(token);
				user.setRestetTokenCreatedDate(new Date());
				loginUser.saveAndFlush(user);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				UserOrgEntity userOrgMap = userOrgRepository.findUserOrgMapByUsername(user.getUsername());

				InitializationTrackerEntity initTracker = initiRepo.getMarksBySclStuExamSubj("datacreation");
				
				if (!user.getRole().getRole().equals("SUPERUSER")) {		
					if(initTracker.isStatus()) {
						response.put("indicator", "fail");
						response.put("message", "Under Maintenance, please try after sometime");
						return new ResponseEntity<>(response, HttpStatus.OK);
					}

					if (user.getRole().getRole().equals("STUDENT")) {
						StudentEntity student = studentRepo.getByStudentIdBySchool(authenticationRequest.getUsername(), userOrgMap.getSchool().getId());
						
						response.put("studentId", Long.toString(student.getId()));
						Cookie cookie5 = new Cookie("studentId", Long.toString(student.getId()));
						cookie5.setMaxAge(60 * 5);
						cookie5.setSecure(false);
						cookie5.setHttpOnly(false);
						cookie5.setPath("/");
						responseMap.addCookie(cookie5);
						
						studentMap = studentMapRepository.getByStudentId(student.getId(), userOrgMap.getSchool().getId());
						if (studentMap != null) {
							response.put("classId", Long.toString(studentMap.getClasses().getId()));
							Cookie cookie3 = new Cookie("classId", Long.toString(studentMap.getClasses().getId()));
							cookie3.setMaxAge(60 * 5);
							cookie3.setSecure(false);
							cookie3.setHttpOnly(false);
							cookie3.setPath("/");
							responseMap.addCookie(cookie3);

							response.put("sectionId", Long.toString(studentMap.getSection().getId()));
							Cookie cookie4 = new Cookie("sectionId", Long.toString(userOrgMap.getSchool().getId()));
							cookie4.setMaxAge(60 * 5);
							cookie4.setSecure(false);
							cookie4.setHttpOnly(false);
							cookie4.setPath("/");
							responseMap.addCookie(cookie4);
						} else {
							response.put("indicator", "fail");
							response.put("message", "User setup not completed, please contact School administrator");
							return new ResponseEntity<>(response, HttpStatus.OK);
						}
					} 
					
					response.put("schoolId", Long.toString(userOrgMap.getSchool().getId()));
					Cookie cookie2 = new Cookie("schoolId", Long.toString(userOrgMap.getSchool().getId()));
					cookie2.setMaxAge(60 * 5);
					cookie2.setSecure(false);
					cookie2.setHttpOnly(false);
					cookie2.setPath("/");
					responseMap.addCookie(cookie2);
				}

				token = jwtTokenUtil.generateToken(userDetails);
				response.put("indicator", "success");
				response.put("token", token);
				response.put("role", user.getRole().getRole());
				
				if(initTracker.isStatus()) {
					response.put("redirecturl", sampleData);
				} else {
					response.put("redirecturl", home);	
				}

				Cookie cookie = new Cookie("token", token);
				cookie.setMaxAge(60 * 5);
				cookie.setSecure(false);
				cookie.setHttpOnly(false);
				cookie.setPath("/");
				responseMap.addCookie(cookie);

				Cookie cookie1 = new Cookie("role", user.getRole().getRole());
				cookie1.setMaxAge(60 * 5);
				cookie1.setSecure(false);
				cookie1.setHttpOnly(false);
				cookie1.setPath("/");
				responseMap.addCookie(cookie1);

				user.setToken(token);
				user.setTokenCreatedDate(new Date());
				loginUser.saveAndFlush(user);

				return new ResponseEntity<>(response, HttpStatus.OK);

				/*
				 * URI google = new URI("http://www.google.com"); HttpHeaders httpHeaders = new
				 * HttpHeaders(); httpHeaders.setLocation(google); return new
				 * ResponseEntity<>(response, httpHeaders, HttpStatus.SEE_OTHER);
				 */

			}

		} catch (AccountExpiredException e) {
			log.error("", e);
			response.put("indicator", "fail");
			message = "User account Expired";
			e.printStackTrace();
		} catch (CredentialsExpiredException e) {
			log.error("", e);
			response.put("indicator", "fail");
			message = "User credentials Expired";
		} catch (DisabledException e) {
			log.error("", e);
			response.put("indicator", "fail");
			message = "User account disabled";
		} catch (LockedException e) {
			log.error("", e);
			response.put("indicator", "fail");
			message = "User account disabled";
		} catch (BadCredentialsException e) {
			log.error("", e);
			response.put("indicator", "fail");
			message = "Incorrect credentials";
		} catch (InternalAuthenticationServiceException e) {
			log.error("", e);
			response.put("indicator", "fail");
			response.put("message", "Incorrect credentials");
			log.error("User Not found: " + authenticationRequest.getUsername());
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		Cookie cookie = new Cookie("token", null);
		cookie.setMaxAge(0);
		cookie.setSecure(false);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		responseMap.addCookie(cookie);

		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	private Authentication authenticate(String username, String password) throws Exception {
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

	}

	@Async("asyncExecutor")
	public void createUser(String username, String password, String role, boolean resetPassword, long schoolId) {
		LoginUser newUser = new LoginUser();
		newUser.setUsername(username);
		newUser.setPassword(bcryptEncoder.encode(password));
		newUser.setAccountNonExpired(true);
		newUser.setAccountNonLocked(true);
		newUser.setFailurecount(0);
		newUser.setEnabled(true);
		newUser.setCredentialsNonExpired(true);
		newUser.setCreatedDate(new Date());
		newUser.setModifiedDate(new Date());
		newUser.setFailurecount(0);
		newUser.setCustomPrevilages(false);
		if (!role.equals("SUPERUSER")) {
			newUser.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
		} else {
			newUser.setSchool(null);
		}
		newUser.setResetPassword(resetPassword);
		newUser.setRole(new UserRoleEntity(role, null));
		LoginUser user = loginUser.saveAndFlush(newUser);
		if (!role.equals("SUPERUSER")) {
			userOrgRepository.saveAndFlush(new UserOrgEntity(0, user, new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null)));
			ouboundRepo.saveAndFlush(new OutboundEntity(0, username + "," + password, "passwordReset", "x`", new Date(), new Date(), false, new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null)));
		}
	}

	@Async("asyncExecutor")
	public void createAdminUser(String password, boolean resetPassword, long schoolId) {

		long count = loginUser.findUsersByAdminRole("ADMIN");
		String username = "ADM" + 10001 + count;

		LoginUser newUser = new LoginUser();
		newUser.setUsername(username);
		newUser.setPassword(bcryptEncoder.encode(password));
		newUser.setAccountNonExpired(true);
		newUser.setAccountNonLocked(true);
		newUser.setEnabled(true);
		newUser.setFailurecount(0);
		newUser.setCredentialsNonExpired(true);
		newUser.setCreatedDate(new Date());
		newUser.setModifiedDate(new Date());
		newUser.setSchool(new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null));
		newUser.setCustomPrevilages(false);
		newUser.setResetPassword(true);
		newUser.setRole(new UserRoleEntity("ADMIN", null));
		LoginUser user = loginUser.saveAndFlush(newUser);
		userOrgRepository.saveAndFlush(new UserOrgEntity(0, user, new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null)));
		ouboundRepo.saveAndFlush(new OutboundEntity(0, username + "," + password, "passwordReset", "email", new Date(), new Date(), false, new SchoolEntity(schoolId, null, null, null, null, null, null, true, null, null, null, null, null, null)));
	}

	public ResponseEntity<?> logout() {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			LoginUser user1 = loginUser.findByUsername(auth.getName());
			user1.setToken("1234567890qwertyuiopasdfghjklzxcvbnm");
			loginUser.saveAndFlush(user1);
			response.put("indicator", "success");
			response.put("message", "Logout successfully");
			response.put("redirecturl", login);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	public ResponseEntity<?> getUsers(long schoolId) {
		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			List<LoginUser> users = loginUser.findUserBySchool(schoolId);

			List<Object> list = new ArrayList<Object>();

			for (int i = 0; i < users.size(); i++) {
				HashMap<String, Object> loginusr = new HashMap<>();
				loginusr.put("username", users.get(i).getUsername());
				loginusr.put("accountNonLocked", users.get(i).isAccountNonLocked());
				loginusr.put("enabled", users.get(i).isEnabled());
				list.add(loginusr);
			}

			response.put("indicator", "success");
			response.put("users", list);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	public ResponseEntity<?> saveUserAccountChanges(UserSettingRequest user, long schoolId) {

		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			LoginUser user1 = loginUser.findByUsername(auth.getName());
			String role = user1.getRole().getRole();
			if (role.equals("SUPERUSER") || role.equals("ADMIN")) {
				LoginUser tuser = loginUser.findByUsernameBySchool(user.getUsername(), schoolId);
				if (tuser != null) {
					tuser.setAccountNonLocked(user.isAccountNonLocked());
					tuser.setEnabled(user.isEnabled());
					loginUser.saveAndFlush(tuser);
					response.put("users", user);
					response.put("indicator", "success");
				} else {
					response.put("indicator", "fail");
					response.put("message", "Username not found");
				}
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("status", 401);
				response.put("message", "Unauthorized");
				response.put("redirecturl", login);
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	public ResponseEntity<?> resetUserPassword(ResetPasswordRequest resetPasss, long schoolId) {

		HashMap<String, Object> response = new HashMap<>();
		String message = null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			LoginUser user1 = loginUser.findByUsername(auth.getName());
			String role = user1.getRole().getRole();
			if (role.equals("SUPERUSER") || role.equals("ADMIN")) {
				LoginUser tuser = loginUser.findByUsernameBySchool(resetPasss.getUsername(), schoolId);
				if (tuser != null) {
					if (resetPasss.getPassword().equals(resetPasss.getPassword1())) {
						tuser.setResetPassword(true);
						tuser.setPassword(bcryptEncoder.encode(resetPasss.getPassword()));
						loginUser.saveAndFlush(tuser);
						response.put("indicator", "success");
						response.put("message", "Temporary Password updated successfully");
					} else {
						response.put("indicator", "fail");
						response.put("message", "Password did not match please try again");
					}

				} else {
					response.put("indicator", "fail");
					response.put("message", "Username not found");
				}
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("status", 401);
				response.put("message", "Unauthorized");
				response.put("redirecturl", login);
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			log.error("", e);
			response.put("indicator", "fail");
			message = "Error Occured, if issue persists please contact administrator";
		}

		if (message != null)
			response.put("message", message);

		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}
	
	//Check if user is authenticated for other apps to continue.
	public Authentication checkUserAuthentication() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth;
	}

}
