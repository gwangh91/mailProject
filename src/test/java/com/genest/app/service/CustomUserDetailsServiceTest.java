package com.genest.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.genest.app.entity.UserJoin;
import com.genest.app.repository.UserRepository;

public class CustomUserDetailsServiceTest {

	/**
	 * CustomUserDetailsService#loadUserByUsername(String)メソッドがユーザーを正常にロードするかどうかをテスト
	 */
	@Test
	public void testLoadUserByUsername_Success() {
		// Mockオブジェクトを使用して、UserRepositoryを生成し、ユーザーを生成
		UserRepository userRepository = mock(UserRepository.class);
		UserJoin userJoin = new UserJoin();
		userJoin.setEmail("test@example.com");
		userJoin.setPassword("password");
		when(userRepository.findByEmail("test@example.com")).thenReturn(userJoin);

		CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService(userRepository);
		// loadUserByUsernameメソッドを呼び出し、ユーザー情報を取得
		UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

		// 読み込んだユーザー情報がnullではなく、予想した値と一致することを確認
		assertNotNull(userDetails);
		assertEquals("test@example.com", userDetails.getUsername());
		assertEquals("password", userDetails.getPassword());
	}

	/**
	 * ユーザーが見つからない場合、UsernameNotFoundExceptionが発生するかどうかをテスト
	 */
	@Test
	public void testLoadUserByUsername_UserNotFound() {
		// UserRepositoryのfindByEmailメソッドがnullを返却するように設定
		UserRepository userRepository = mock(UserRepository.class);
		when(userRepository.findByEmail(anyString())).thenReturn(null);

		CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService(userRepository);

		// loadUserByUsernameメソッドを呼び出す際に例外が発生することを確認
		assertThrows(UsernameNotFoundException.class, () -> {
			customUserDetailsService.loadUserByUsername("test123@example.com");
		});
	}

	/**
	 * データベースで例外が発生する場合、UsernameNotFoundExceptionが発生するかどうかをテスト
	 */
	@Test
	public void testLoadUserByUsername_ExceptionThrown() {
		// UserRepositoryのfindByEmailメソッドが例外を発生させるように設定
		UserRepository userRepository = mock(UserRepository.class);
		when(userRepository.findByEmail(anyString())).thenThrow(new RuntimeException("Database error"));

		CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService(userRepository);

		// loadUserByUsernameメソッドを呼び出す際に例外が発生することを確認
		assertThrows(UsernameNotFoundException.class, () -> {
			customUserDetailsService.loadUserByUsername("test@example.com");
		});
	}
}