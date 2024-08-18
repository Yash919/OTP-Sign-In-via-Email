package com.otp.verification.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.otp.verification.Entity.User;
import com.otp.verification.Service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String loginPage() {
		return "login"; // This should map to src/main/resources/templates/login.html
	}
	@GetMapping("/createUser")
	public String showCreateUserPage() {
		return "createUser";
	}
	@GetMapping("/otp")
	public String otpPage(@RequestParam("email") String email, Model model) {
		model.addAttribute("email", email);
		return "otp"; // This should map to src/main/resources/templates/otp.html
	}
	@GetMapping("/dashboard")
	public String getDashboard(@RequestParam("email") String email, Model model) {
		User user = userService.findByEmail(email)
							   .orElseThrow(() -> new RuntimeException("User not found"));
		model.addAttribute("user", user);
		return "dashboard";
	}
	@GetMapping("/forgot-password")
	public String showForgotPasswordPage() {
		return "forgot-password";
	}

	@GetMapping("/reset-password")
	public String showResetPasswordPage() {
		return "reset-password";
	}

}