package com.genest.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.genest.app.repository.UserRepository;
import com.genest.app.util.Constants;

@RestController
public class RegistrationController {

	private final UserRepository userRepository;

	public RegistrationController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/join-check-email")
	public Map<String, Boolean> checkEmail(@RequestParam("email") String email) {
		Map<String, Boolean> response = new HashMap<>();

		// メール形式チェック
		if (!isValidEmail(email)) {
			response.put("formatError", true);
			return response;
		}

		// メール重複チェック
		Boolean isExist = userRepository.existsByEmail(email);
		response.put("exists", isExist);

		return response;
	}

	// メールの正規式をチェックするメソッド
	public boolean isValidEmail(String email) {
		return email.matches(Constants.EMAIL_REGEX);
	}
}
