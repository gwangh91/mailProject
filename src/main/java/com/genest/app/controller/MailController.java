package com.genest.app.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.genest.app.dto.MailDTO;
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
	public String confrimMailForm(@ModelAttribute MailDTO mailDTO, Model model) {

		model.addAttribute("mailDTO", mailDTO);
		// メール送信確認ページへ遷移
		return Constants.CONFIRM_MAIL_FORM_VIEW;
	}

	@PostMapping("/sendMail")
	public String sendMail(@ModelAttribute @Valid MailDTO mailDTO, Model model) {
		mailService.sendEmail(mailDTO);
		model.addAttribute("message", Constants.SUCCESS_MAIL_MESSAGE);
		// メール送信完了ページへ遷移
		return Constants.MAIL_RESULT_VIEW;
	}
}
