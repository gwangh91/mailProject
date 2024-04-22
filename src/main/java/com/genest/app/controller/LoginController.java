package com.genest.app.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.genest.app.dto.CustomUserDetails;
import com.genest.app.dto.JoinDTO;
import com.genest.app.jwt.JWTUtil;
import com.genest.app.util.Constants;

@Controller
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;

	public LoginController(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@GetMapping("/login-form")
	public String showLoginForm() {
		return Constants.LOGIN_FORM_VIEW;
	}

	@PostMapping("/loging")
	public ResponseEntity<?> loginProcess(@RequestBody JoinDTO joinDTO) {
		String email = joinDTO.getEmail();
		String password = joinDTO.getPassword();

		try {

			Authentication authToken = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(email, password));

			SecurityContextHolder.getContext().setAuthentication(authToken);

			CustomUserDetails userDetails = (CustomUserDetails) authToken.getPrincipal();

			String token = jwtUtil.createJwt(userDetails.getUsername(),
					userDetails.getAuthorities().iterator().next().getAuthority(), 60 * 60 * 1L);

			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + token);
			return ResponseEntity.ok(Map.of("token", token));
		} catch (BadCredentialsException e) {
			// パスワードが間違っている場合の例外処理
			logger.error("Bad credentials: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", "Bad credentials: " + e.getMessage()));
		} catch (AuthenticationException e) {
			logger.error("Authentication failed: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", "Authentication failed: " + e.getMessage()));
		} catch (Exception e) {
			logger.error("An error occurred: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "An error occurred: " + e.getMessage()));
		}

	}

}