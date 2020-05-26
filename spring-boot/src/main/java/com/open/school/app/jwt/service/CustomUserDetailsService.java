package com.open.school.app.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.open.school.app.jwt.config.CustomUserDetails;
import com.open.school.app.jwt.repository.LoginUserRepository;

@Configuration
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private LoginUserRepository loginUser;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		CustomUserDetails user = new CustomUserDetails(loginUser.findByUsername(username));
		return user;
	}

}
