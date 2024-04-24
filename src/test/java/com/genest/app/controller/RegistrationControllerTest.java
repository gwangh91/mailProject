package com.genest.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.genest.app.repository.UserRepository;

public class RegistrationControllerTest {

	private UserRepository userRepository;
	private RegistrationController registrationController;

	@BeforeEach
	public void setUp() {
		// UserRepositoryのmockオブジェクトとRegistrationControllerオブジェクトを生成
		userRepository = mock(UserRepository.class);
		registrationController = new RegistrationController(userRepository);
	}

	/**
	 * checkEmailメソッドで有効なメールを提供する場合の動作を検証するテスト ケース
	 */
	@Test
	public void testCheckEmail_ValidEmail() {
		String validEmail = "test@example.com";
		// UserRepositoryのexistsByEmailメソッドがfalseを返却するように設定
		when(userRepository.existsByEmail(validEmail)).thenReturn(false);

		// checkEmailメソッドを呼び出し、応答を取得
		Map<String, Boolean> response = registrationController.checkEmail(validEmail);

		// 応答が適切であることを確認
		assertEquals(1, response.size());
		assertEquals(false, response.get("exists"));
	}

	/**
	 * checkEmailメソッドで無効なメールを提供する場合の動作を検証するテスト ケース
	 */
	@Test
	public void testCheckEmail_InvalidEmail() {
		String invalidEmail = "invalidemail";
		// checkEmailメソッドを呼び出し、応答を取得
		Map<String, Boolean> response = registrationController.checkEmail(invalidEmail);

		// 応答が適切であることを確認
		assertEquals(1, response.size());
		assertEquals(true, response.get("formatError"));
	}

	/**
	 * checkEmailメソッドですでに存在するメールを提供する場合の動作を検証するテスト
	 */
	@Test
	public void testCheckEmail_duplicate() {
		String existingEmail = "test@gmail.com";
		// UserRepositoryのexistsByEmailメソッドがtrueを返却するように設定
		when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

		// checkEmailメソッドを呼び出し、応答を取得
		Map<String, Boolean> response = registrationController.checkEmail(existingEmail);

		// 応答が適切であることを確認
		assertEquals(1, response.size());
		assertEquals(true, response.get("exists"));
	}
}