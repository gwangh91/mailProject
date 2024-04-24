package com.genest.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.genest.app.util.Constants;

@Controller
@SpringBootApplication
public class MailApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailApplication.class, args);
	}

	@GetMapping("/")
	public String showLoginForm() {
		return Constants.LOGIN_FORM_VIEW;
	}
}
