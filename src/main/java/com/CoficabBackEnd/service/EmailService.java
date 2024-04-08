package com.CoficabBackEnd.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendEmail(String to, String subject, String content) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

		try {
			helper.setFrom("noreply@example.com");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true); // Set the second parameter to true for HTML content
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			// Handle the exception as needed
			e.printStackTrace();
		}
	}

	public void sendPasswordResetEmail(String to, String resetLink) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");

			ClassPathResource image1Resource = new ClassPathResource("static/assets/img/image-1.png");
			ClassPathResource image2Resource = new ClassPathResource("static/assets/img/image-2.png");

			// Add images as inline attachments
			helper.addInline("image1", image1Resource);
			helper.addInline("image2", image2Resource);

			String emailContent = "reset password content";
			sendEmail(to, "Password Reset", emailContent);
		} catch (MessagingException e) {
			// Handle the exception as needed
			e.printStackTrace();
		}
	}

	public void sendLoginVerificationCodeEmail(String to, String code) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            // Set email details
            helper.setFrom("noreply@example.com");
            helper.setTo(to);
            helper.setSubject("Login Verification Code");
            helper.setText("Your verification code is: " + code);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
