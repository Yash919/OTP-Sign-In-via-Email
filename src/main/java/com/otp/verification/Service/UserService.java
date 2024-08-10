package com.otp.verification.Service;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.otp.verification.Entity.User;
import com.otp.verification.Repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

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
}