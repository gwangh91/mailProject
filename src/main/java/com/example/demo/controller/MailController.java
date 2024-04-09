package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.service.MailService;

@Controller
public class MailController {

	private MailService mailService;
	
	public MailController(MailService mailService) {
		this.mailService = mailService;
	}
	
	@GetMapping("/mailForm")
	public String showMailForm() {
		return "mailForm";
	}
	
	@PostMapping("/confrimMailForm")
	public String confrimMailForm(String to, String subject, String body, Model model) {
		
		model.addAttribute("to", to);
		model.addAttribute("subject", subject);
		model.addAttribute("body", body);
		
		return "confrimMailForm";
	}

	@PostMapping("/sendMail")
	public String sendMail(String to, String subject, String body, Model model) {
		mailService.sendEmail(to, subject, body);
		model.addAttribute("message", "転送成功");
		return "mailResult";
	}
}
