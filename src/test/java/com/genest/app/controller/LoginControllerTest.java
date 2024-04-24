package com.genest.app.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.genest.app.dto.CustomUserDetails;
import com.genest.app.dto.JoinDTO;
import com.genest.app.jwt.JWTUtil;
import com.genest.app.util.Constants;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JWTUtil jwtUtil;

	@Mock
	private CustomUserDetails userDetails;

	@InjectMocks
	private LoginController loginController;

	@Test
	public void testShowLoginForm() {
		String viewName = loginController.showLoginForm();
		assertThat(viewName).isEqualTo(Constants.LOGIN_FORM_VIEW);
	}

	/**
	 * loginProcess(JoinDTO)メソッドが正常にログインする場合を検証
	 */
	@Test
	public void testLoginProcess_Success() {
		// 有効なJoinDTOオブジェクトを生成
		JoinDTO joinDTO = new JoinDTO();
		joinDTO.setEmail("test@example.com");
		joinDTO.setPassword("password");

		// AuthenticationManagerおよびJWTUtilのmockオブジェクトの動作を設定
		Authentication authentication = mock(Authentication.class);
		when(authenticationManager.authenticate(any())).thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(new CustomUserDetails()); // getPrincipal() 호출 시
																					// CustomUserDetails 반환

		when(jwtUtil.createJwt(null, null, 3600L)).thenReturn("dummy_token");

		// loginProcessメソッドを呼び出し、返されたResponseEntityを取得
		ResponseEntity<?> response = loginController.loginProcess(joinDTO);

		// 返されたResponseEntityが予想したHttpStatusおよびbodyを持っているか確認
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Collections.singletonMap("token", "dummy_token"), response.getBody());
	}

	/**
	 * loginProcessメソッドが間違った資格情報の例外を処理しているかどうかを検証
	 */
	@Test
	public void testLoginProcess_BadCredentialsException() {
		// 間違った資格証明例外をを発生するように設定
		JoinDTO joinDTO = new JoinDTO();
		joinDTO.setEmail("test@example.com");
		joinDTO.setPassword("wrong_password");

		when(authenticationManager.authenticate(Mockito.any()))
				.thenThrow(new BadCredentialsException("Bad credentials"));

		// loginProcessメソッドを呼び出して返されたResponseEntityを取得
		ResponseEntity<?> response = loginController.loginProcess(joinDTO);

		// 返されたResponseEntityが予想したHttpStatusおよびbodyを持っているかを確認
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(response.getBody()).isInstanceOf(Map.class);
		assertThat(((Map<?, ?>) response.getBody()).get("error")).isEqualTo("Bad credentials: Bad credentials");
	}

	/**
	 * loginProcessメソッドが認証例外を処理するかを検証
	 */
	@Test
	public void testLoginProcess_AuthenticationException() {
		// 認証例外を発生させるように設定
		JoinDTO joinDTO = new JoinDTO();
		joinDTO.setEmail("test@example.com");
		joinDTO.setPassword("password");

		when(authenticationManager.authenticate(Mockito.any()))
				.thenThrow(new AuthenticationException("Authentication failed") {
				});

		// loginProcessメソッドを呼び出して返されたResponseEntityを取得
		ResponseEntity<?> response = loginController.loginProcess(joinDTO);

		// 返されたResponseEntityが予想したHttpStatusおよびbodyを持っているかを確認
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(response.getBody()).isInstanceOf(Map.class);
		assertThat(((Map<?, ?>) response.getBody()).get("error"))
				.isEqualTo("Authentication failed: Authentication failed");
	}

	/**
	 * loginProcessメソッドが一般的な例外を処理するかどうかを検証
	 */
	@Test
	public void testLoginProcess_Exception() {
		// 認証例外を発生させるように設定
		JoinDTO joinDTO = new JoinDTO();
		joinDTO.setEmail("test@example.com");
		joinDTO.setPassword("password");

		// Mocking generic exception
		when(authenticationManager.authenticate(Mockito.any()))
				.thenThrow(new RuntimeException("Internal server error"));

		// loginProcessメソッドを呼び出して返されたResponseEntityを取得
		ResponseEntity<?> response = loginController.loginProcess(joinDTO);

		// 返されたResponseEntityが予想したHttpStatusおよびbodyを持っているかを確認
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(response.getBody()).isInstanceOf(Map.class);
		assertThat(((Map<?, ?>) response.getBody()).get("error")).isEqualTo("An error occurred: Internal server error");
	}
}