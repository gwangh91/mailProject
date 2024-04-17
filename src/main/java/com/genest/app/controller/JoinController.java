package com.genest.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.genest.app.dto.JoinDTO;
import com.genest.app.service.JoinService;
import com.genest.app.util.Constants;

@Controller
public class JoinController {

	private final JoinService joinService;

	public JoinController(JoinService joinService) {
		this.joinService = joinService;
	}

	@GetMapping("/join-form")
	public String showJoinForm() {
		return Constants.JOIN_FORM_VIEW;
	}

	@PostMapping("/join")
	public String joinProcess(JoinDTO joinDTO) {

		joinService.joinProcess(joinDTO);

		return "redirect:/join-popup";
	}

	@GetMapping("/join-popup")
	public String joinPopup() {
		return Constants.JOIN_POPUP_VIEW;
	}
}