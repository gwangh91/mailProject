package com.genest.app.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.genest.app.util.Constants;

@Controller
public class GlobalExceptionHandler implements ErrorController {

	@GetMapping(Constants.ERROR_PATH)
	public String handleError(Model model) {

		model.addAttribute("message", Constants.ERROR_MESSAGE);
		// エラーページへ遷移
		return Constants.ERROR_VIEW;
	}

}