package com.otp.verification.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.otp.verification.Entity.PasswordResetToken;
import com.otp.verification.Entity.User;
import com.otp.verification.Repository.PasswordResetTokenRepository;
import com.otp.verification.Repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordResetTokenRepository tokenRepository;
	@Autowired
	private EmailService emailService;
	@Value("${password.reset.token.expiration}") // e.g., 1 hour
	private long tokenExpirationTime;

	public void createUser(String username,String email, String password, String bio, String linkedInUrl, String githubUrl, String twitterUrl){
		if (username == null || username.trim().isEmpty()) {
			throw new IllegalArgumentException("Username cannot be null or empty");
		}
		if (password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("Password cannot be null or empty");
		}
		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("Email cannot be null or empty");
		}
		if (userRepository.findByEmail(email).isPresent()) {
			throw new IllegalArgumentException("Email already exists");
		}
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(hashPassword(password));
		user.setBio(bio);
		user.setLinkedInUrl(linkedInUrl);
		user.setGithubUrl(githubUrl);
		user.setTwitterUrl(twitterUrl);
		userRepository.save(user);
	}

	private static String hashPassword(String plainPassword){
		return BCrypt.hashpw(plainPassword,BCrypt.gensalt());
	}

	public Optional<User> findByEmail(String email){
		return userRepository.findByEmail(email);
	}

	public String createPasswordResetToken(String email) {
		Optional<User> userOptional = userRepository.findByEmail(email);
		if (!userOptional.isPresent()) {
			return null; // User not found
		}
		User user = userOptional.get();
		String token = UUID.randomUUID().toString(); // Generate a unique token
		PasswordResetToken resetToken = new PasswordResetToken(token, user, LocalDateTime.now().plusHours(tokenExpirationTime));
		tokenRepository.save(resetToken);
		return token;
	}

	public void sendPasswordResetEmail(String email, String token) {
		String resetLink = "http://localhost:8080/reset-password?token=" + token;
		String subject = "Password Reset Request";
		String body = "To reset your password, please click the following link: " + resetLink;
		emailService.sendEmail(email, subject, body);
	}

}