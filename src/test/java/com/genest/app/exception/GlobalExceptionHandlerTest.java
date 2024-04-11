package com.genest.app.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import com.genest.app.util.Constants;

public class GlobalExceptionHandlerTest {

	@Test
	public void testHandleError() {
		GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
		Model model = mock(Model.class);

		String result = globalExceptionHandler.handleError(model);

		assertEquals(Constants.ERROR_VIEW, result);
		verify(model).addAttribute("message", Constants.ERROR_MESSAGE);
	}
}