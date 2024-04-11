package com.genest.app.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import com.genest.app.FakeMailService;
import com.genest.app.dto.MailDTO;
import com.genest.app.repository.SentMailRepository;

import jakarta.mail.MessagingException;

public class MailServiceTest {

	private FakeMailService fakeMailService;

	@BeforeEach
	void setUp() {
		JavaMailSender javaMailSender = null;
		SentMailRepository sentMailRepository = null;
		fakeMailService = new FakeMailService(javaMailSender, sentMailRepository);
	}

	@Test
	void testSendEmail() throws MessagingException {
		MailDTO mailDTO = new MailDTO();
		mailDTO.setTo("test@gmail.com");
		mailDTO.setSubject("Subject");
		mailDTO.setBody("Body");

		fakeMailService.sendEmail(mailDTO);

		assertTrue(fakeMailService.isSendEmailCalled());
	}
}