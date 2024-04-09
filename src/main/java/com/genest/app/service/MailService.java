package com.genest.app.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.genest.app.entity.SentMail;
import com.genest.app.repository.SentMailRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class MailService {

	private JavaMailSender javaMailSender;
    private SentMailRepository sentMailRepository;
	
	public MailService(JavaMailSender javaMailSender, SentMailRepository sentMailRepository) {
		this.javaMailSender = javaMailSender;
		this.sentMailRepository = sentMailRepository;
	}
	
	public void sendEmail(String to, String subject, String body) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		try {
			// メール転送
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			javaMailSender.send(message);
			
			// メール転送後、DBに保存
			SentMail sentMail = new SentMail();
			sentMail.setRecipientEmail(to);
			sentMail.setSubject(subject);
			sentMail.setBody(body);
			sentMailRepository.save(sentMail);
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
