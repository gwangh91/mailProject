package com.genest.app.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.genest.app.dto.JoinDTO;
import com.genest.app.entity.UserJoin;
import com.genest.app.repository.UserRepository;

@Service
public class JoinService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public void joinProcess(JoinDTO joinDTO) {
		String email = joinDTO.getEmail();
		String password = joinDTO.getPassword();

		Boolean isExist = userRepository.existsByEmail(email);

		if (isExist) {
			return;
		}

		UserJoin userJoin = new UserJoin();

		userJoin.setEmail(email);
		userJoin.setPassword(bCryptPasswordEncoder.encode(password));

		userRepository.save(userJoin);
	}
}