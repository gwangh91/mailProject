package com.genest.app;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.genest.app.dto.MailDTO;
import com.genest.app.entity.SentMail;
import com.genest.app.repository.SentMailRepository;
import com.genest.app.service.MailService;

import jakarta.mail.MessagingException;

@Service
public class FakeMailService extends MailService {

	private boolean isSendEmailCalled = false;

	public FakeMailService(JavaMailSender javaMailSender, SentMailRepository sentMailRepository) {
		super(javaMailSender, sentMailRepository);
	}

	@Override
	public void sendEmail(MailDTO mailDTO) throws MessagingException {
		isSendEmailCalled = true;
		SentMail sentMail = new SentMail();
		sentMail.setRecipientEmail(mailDTO.getTo());
		sentMail.setSubject(mailDTO.getSubject());
		sentMail.setBody(mailDTO.getBody());
	}

	public boolean isSendEmailCalled() {
		return isSendEmailCalled;
	}
}