package com.genest.app.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.genest.app.dto.CustomUserDetails;
import com.genest.app.entity.UserJoin;
import com.genest.app.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		// DBで照会
		UserJoin userData = userRepository.findByEmail(email);

		if (userData != null) {
			// UserDetailsに入れてreturnするとAutneticationManagerが検証する
			return new CustomUserDetails(userData);
		}
		return null;
	}

}