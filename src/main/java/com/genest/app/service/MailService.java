package com.genest.app.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.genest.app.dto.MailDTO;
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

	public void sendEmail(MailDTO mailDTO) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		try {
			// メール転送
			helper.setTo(mailDTO.getTo());
			helper.setSubject(mailDTO.getSubject());
			helper.setText(mailDTO.getBody(), true);
			javaMailSender.send(message);

			// メール転送後、DBに保存
			SentMail sentMail = new SentMail();
			sentMail.setRecipientEmail(mailDTO.getTo());
			sentMail.setSubject(mailDTO.getSubject());
			sentMail.setBody(mailDTO.getBody());
			sentMailRepository.save(sentMail);

		} catch (MessagingException e) {
			throw new MessagingException("メールの送信に失敗しました。", e);
		}
	}
}
