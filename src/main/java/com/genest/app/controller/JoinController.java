package com.genest.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

	@PostMapping("/join-registration")
	public String joinProcess(JoinDTO joinDTO, Model model) {
		try {
			joinService.joinProcess(joinDTO);
			model.addAttribute("message", Constants.SUCCESS_JOIN_MESSAGE);
			return Constants.JOIN_POPUP_VIEW;
		} catch (Exception e) {
			model.addAttribute("message", Constants.FAIL_JOIN_MESSAGE);
			return Constants.JOIN_POPUP_VIEW;
		}
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		return ResponseEntity.badRequest().body(errors);
	}
}