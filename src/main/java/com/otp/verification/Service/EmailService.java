package com.otp.verification.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendOtpEmail(String email, String userName, String otpCode) {
		String subject = "Your OTP Code Request";
		String body = String.format("Dear %s,\n\nWe noticed that you've requested a new OTP code. Your OTP is:\n\n**%s**\n\nThis OTP is valid for the next 5 minutes. Please enter it on the verification page to proceed.\n\nIf you did not request this code, please ignore this email. However, if you keep receiving OTPs you did not request, we recommend updating your account security settings.\n\nBest regards,\nYash a.k.a (🦁)", userName, otpCode);

		sendEmail(email, subject, body);
	}

	public void sendPasswordResetEmail(String email, String userName, String token) {
		String resetLink = "http://localhost:8080/reset-password?token=" + token;
		String subject = "Password Reset Request";
		String body = String.format("Dear %s,\n\nWe received a request to reset your password. To proceed with resetting your password, please click the link below:\n\n%s\n\nThis link will expire in 1 hour. If you did not request a password reset, please ignore this email. We recommend ensuring your account's security by updating your password regularly.\n\nIf you encounter any issues, feel free to contact our support team.\n\nBest regards,\nYash a.k.a (🦁)", userName, resetLink);

		sendEmail(email, subject, body);
	}

	public void sendEmail(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
	}
}