package com.genest.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.genest.app.service.MailService;
import com.genest.app.util.Constants;

@Controller
public class MailController {

	private MailService mailService;
	
	public MailController(MailService mailService) {
		this.mailService = mailService;
	}
	
	@GetMapping("/mailForm")
	public String showMailForm() {
		return Constants.MAIL_FORM_VIEW;
	}
	
	@PostMapping("/confirmMailForm")
	public String confrimMailForm(String to, String subject, String body, Model model) {
		
		model.addAttribute("to", to);
		model.addAttribute("subject", subject);
		model.addAttribute("body", body);
		
		return Constants.CONFIRM_MAIL_FORM_VIEW;
	}

	@PostMapping("/sendMail")
	public String sendMail(String to, String subject, String body, Model model) {
		mailService.sendEmail(to, subject, body);
		model.addAttribute("message", Constants.SUCCESS_MAIL_MESSAGE);
		return Constants.MAIL_RESULT_VIEW;
	}
}
