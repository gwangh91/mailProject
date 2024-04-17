package com.genest.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.genest.app.util.Constants;

@Controller
public class LoginController {

	@GetMapping("/login-form")
	public String showLoginForm() {
		return Constants.LOGIN_FORM_VIEW;
	}

}