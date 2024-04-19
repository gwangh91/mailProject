package com.genest.app.service;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.genest.app.dto.JoinDTO;
import com.genest.app.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class JoinServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@InjectMocks
	private JoinService joinService;

	/**
	 * joinProcessメソッド動作を検証するテストケース
	 */
	@Test
	void testJoinProcess() {
		// JoinDTOオブジェクト生成
		JoinDTO joinDTO = new JoinDTO();
		joinDTO.setEmail("test@example.com");
		joinDTO.setPassword("12345");

		// bCryptPasswordEncoder.encode("12345")が呼び出される時に"encryptedPassword"を返す
		when(bCryptPasswordEncoder.encode("12345")).thenReturn("encryptedPassword");

		joinService.joinProcess(joinDTO);

		verify(userRepository).save(argThat(userJoin -> "test@example.com".equals(userJoin.getEmail())
				&& "encryptedPassword".equals(userJoin.getPassword())));
	}
}