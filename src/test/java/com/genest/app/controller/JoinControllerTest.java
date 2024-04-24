package com.genest.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.genest.app.dto.JoinDTO;
import com.genest.app.service.JoinService;
import com.genest.app.util.Constants;

@SpringBootTest
@AutoConfigureMockMvc
public class JoinControllerTest {

	private MockMvc mockMvc;
	private JoinService joinService;
	private JoinController joinController;

	@BeforeEach
	public void setup() {
		joinService = mock(JoinService.class);
		joinController = new JoinController(joinService);
		mockMvc = MockMvcBuilders.standaloneSetup(joinController).build();
	}

	/**
	 * showJoinFormメソッド動作を検証するテストケース
	 * 
	 * @throws Exception
	 */
	@Test
	public void testshowJoinForm() throws Exception {
		mockMvc.perform(get("/join-form")).andExpect(status().isOk()).andExpect(view().name(Constants.JOIN_FORM_VIEW));
	}

	/**
	 * joinProcessメソッドが正常に処理される場合の動作を検証するテストケース
	 */
	@Test
	public void testJoinProcessSuccess() {
		// Setup
		JoinDTO joinDTO = new JoinDTO();
		Model model = mock(Model.class);

		// When
		String viewName = joinController.joinProcess(joinDTO, model);

		// Then
		verify(joinService).joinProcess(joinDTO);
		verify(model).addAttribute("message", Constants.SUCCESS_JOIN_MESSAGE);
		assertEquals(Constants.JOIN_POPUP_VIEW, viewName);
	}

	/**
	 * joinProcessメソッドが失敗した場合の動作を検証するテストケース
	 */
	@Test
	public void testJoinProcessFailure() {
		// Setup
		JoinDTO joinDTO = new JoinDTO();
		Model model = mock(Model.class);
		doThrow(new RuntimeException("Join failed")).when(joinService).joinProcess(joinDTO);

		// When
		String viewName = joinController.joinProcess(joinDTO, model);

		// Then
		verify(model).addAttribute("message", Constants.FAIL_JOIN_MESSAGE);
		assertEquals(Constants.JOIN_POPUP_VIEW, viewName);
	}

	/**
	 * bin フィールドで有効性例外を処理するテストケース
	 */
	@Test
	public void testHandleValidationExceptionWithBlank() {
		JoinController testJoinController = new JoinController(joinService);
		MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
		BindingResult bindingResult = mock(BindingResult.class);

		// Field Errorオブジェクト作成
		FieldError emailError = new FieldError("joinDTO", "email", "メールを入力してください。");
		FieldError passwordError = new FieldError("joinDTO", "password", "パスワードを入力してください。");
		FieldError confirmPasswordError = new FieldError("joinDTO", "confirmPassword", "パスワード確認を入力してください。");

		when(ex.getBindingResult()).thenReturn(bindingResult);
		when(bindingResult.getFieldErrors()).thenReturn(List.of(emailError, passwordError, confirmPasswordError));

		ResponseEntity<Map<String, String>> responseEntity = testJoinController.handleValidationException(ex);

		// 予想される応答生成
		Map<String, String> expectedErrors = new HashMap<>();
		expectedErrors.put("email", "メールを入力してください。");
		expectedErrors.put("password", "パスワードを入力してください。");
		expectedErrors.put("confirmPassword", "パスワード確認を入力してください。");

		assertEquals(expectedErrors, responseEntity.getBody());
		assertEquals(400, responseEntity.getStatusCode().value());
	}

	/**
	 * 有効性例外を処理するテストケース
	 */
	@Test
	public void testHandleValidationException() {
		JoinController testJoinController = new JoinController(joinService);
		MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
		BindingResult bindingResult = mock(BindingResult.class);

		// FieldErrorオブジェクト作成
		FieldError emailError = new FieldError("joinDTO", "email", "メールの形式が正しくありません。");
		FieldError passwordError = new FieldError("joinDTO", "password", "パスワードは5文字以上入力してください。");

		when(ex.getBindingResult()).thenReturn(bindingResult);
		when(bindingResult.getFieldErrors()).thenReturn(List.of(emailError, passwordError));

		ResponseEntity<Map<String, String>> responseEntity = testJoinController.handleValidationException(ex);

		// 予想される応答生成
		Map<String, String> expectedErrors = new HashMap<>();
		expectedErrors.put("email", "メールの形式が正しくありません。");
		expectedErrors.put("password", "パスワードは5文字以上入力してください。");

		// 予想した値と返された値の比較
		assertEquals(expectedErrors, responseEntity.getBody());
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

}