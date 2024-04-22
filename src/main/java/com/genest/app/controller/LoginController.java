package com.genest.app.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.genest.app.dto.CustomUserDetails;
import com.genest.app.jwt.JWTUtil;
import com.genest.app.util.Constants;

@Controller
public class LoginController {

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
	public ModelAndView loginProcess(@RequestParam("email") String email, @RequestParam("password") String password) {
		ModelAndView modelAndView = new ModelAndView();

		try {

			Authentication authToken = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(email, password));

			SecurityContextHolder.getContext().setAuthentication(authToken);

			CustomUserDetails userDetails = (CustomUserDetails) authToken.getPrincipal();

			String token = jwtUtil.createJwt(userDetails.getUsername(),
					userDetails.getAuthorities().iterator().next().getAuthority(), 60 * 60 * 1L);

			modelAndView.addObject("token", token);
			modelAndView.setViewName(Constants.MAIL_FORM_VIEW);
		} catch (Exception e) {
			modelAndView.setViewName("error");
			e.printStackTrace();
		}

		return modelAndView;
	}

}