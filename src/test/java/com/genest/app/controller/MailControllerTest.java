package com.genest.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.genest.app.dto.MailDTO;
import com.genest.app.service.MailService;
import com.genest.app.util.Constants;

import jakarta.mail.MessagingException;

public class MailControllerTest {

	private MailController mailController;
	private MailService mailService;

	@BeforeEach
	public void setup() {
		mailService = mock(MailService.class);
		mailController = new MailController(mailService);
	}

	@Test
	public void testShowMailForm() {
		String result = mailController.showMailForm();
		assertEquals(Constants.MAIL_FORM_VIEW, result);
	}

	@Test
	public void testConfirmMailForm() {
		MailDTO mailDTO = new MailDTO();
		Model model = mock(Model.class);
		String result = mailController.confrimMailForm(mailDTO, model);
		assertEquals(Constants.CONFIRM_MAIL_FORM_VIEW, result);
	}

	@Test
	public void testSendMail_Success() {
		MailDTO mailDTO = new MailDTO();
		Model model = mock(Model.class);
		String result = mailController.sendMail(mailDTO, model);
		assertEquals(Constants.MAIL_RESULT_VIEW, result);
	}

	@Test
	public void testSendMail_Fail() throws MessagingException {
		MailDTO mailDTO = new MailDTO();
		Model model = mock(Model.class);

		doThrow(new MessagingException("Test MessagingException")).when(mailService).sendEmail(mailDTO);
		String result = mailController.sendMail(mailDTO, model);
		assertEquals(Constants.MAIL_ERROR_VIEW, result);
		assertEquals(Constants.ERROR_MAIL_MESSAGE, "メール送信中にエラーが発生しました。");
	}

	@Test
	public void testHandleValidationException() {
		MailController testMailController = new MailController(null);
		MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
		BindingResult bindingResult = mock(BindingResult.class);

		// Field Errorオブジェクト作成
		FieldError toError = new FieldError("mailDTO", "to", "Toフィールドは必須です。");
		FieldError subjectError = new FieldError("mailDTO", "subject", "subjectフィールドは必須です。");
		FieldError bodyError = new FieldError("mailDTO", "body", "bodyフィールドは必須です。");

		when(ex.getBindingResult()).thenReturn(bindingResult);
		when(bindingResult.getFieldErrors()).thenReturn(List.of(toError, subjectError, bodyError));

		ResponseEntity<Map<String, String>> responseEntity = testMailController.handleValidationException(ex);

		// 予想される応答生成
		Map<String, String> expectedErrors = new HashMap<>();
		expectedErrors.put("to", "Toフィールドは必須です。");
		expectedErrors.put("subject", "subjectフィールドは必須です。");
		expectedErrors.put("body", "bodyフィールドは必須です。");

		assertEquals(expectedErrors, responseEntity.getBody());
		assertEquals(400, responseEntity.getStatusCode().value());
	}
}