package com.otp.verification.Service;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.otp.verification.Entity.PasswordResetToken;
import com.otp.verification.Entity.User;
import com.otp.verification.Repository.PasswordResetTokenRepository;
import com.otp.verification.Repository.UserRepository;

@Service
public class PasswordResetTokenService {

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmailService emailService;

	public PasswordResetToken validatePasswordResetToken(String token) {
		Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(token);

		if (tokenOptional.isPresent()) {
			PasswordResetToken resetToken = tokenOptional.get();
			if (!resetToken.isExpired()) {
				return resetToken;
			}
		}
		return null; // Token is invalid or expired
	}

	public boolean resetPassword(String token, String newPassword) {
		PasswordResetToken resetToken = validatePasswordResetToken(token);

		if (resetToken != null) {
			User user = resetToken.getUser();
			// Encode the new password before setting it
			String encodedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
			user.setPassword(encodedPassword);
			userRepository.save(user);
			// Optionally, delete the token after successful password reset
			passwordResetTokenRepository.delete(resetToken);
			// Send password update confirmation email to the user
			sendPasswordUpdateConfirmationEmail(user.getEmail(), user.getUsername());
			return true;
		}
		return false;
	}

	private void sendPasswordUpdateConfirmationEmail(String email, String username) {

		String subject = "Your Password Has Been Successfully Updated";
		String body = "Dear " + username + ",\n\n"
				+ "We wanted to let you know that your password was updated successfully. "
				+ "If you did not make this change, please contact our support team immediately.\n\n"
				+ "Thank you,\n"
				+ "Yash a.k.a (🦁)";
		emailService.sendEmail(email, subject, body);
	}
}