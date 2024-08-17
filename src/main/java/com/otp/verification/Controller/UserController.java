package com.otp.verification.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otp.verification.Entity.User;
import com.otp.verification.Service.OtpService;
import com.otp.verification.Service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private OtpService otpService;

	@PostMapping("/create")
	public ResponseEntity<Map<String, String>> createUser(@RequestBody User user) {
		Map<String, String> response = new HashMap<>();
		try {
			userService.createUser(user.getUsername(), user.getEmail(), user.getPassword(), user.getBio(), user.getLinkedInUrl(), user.getGithubUrl(), user.getTwitterUrl());
			response.put("message", "User created successfully!");
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (IllegalArgumentException e) {
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody Map<String, String> loginRequest) {
		String email = loginRequest.get("email");
		String password = loginRequest.get("password");

		Optional<User> userOptional = userService.findByEmail(email);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			if (BCrypt.checkpw(password, user.getPassword())) {
				return ResponseEntity.status(HttpStatus.OK).body("/otp?email=" + email); // Redirect to /otp
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}
	}

}