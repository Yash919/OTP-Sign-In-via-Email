package com.otp.verification.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.otp.verification.Service.OtpService;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

	@Autowired
	private OtpService otpService;

	@PostMapping("/generate")
	public ResponseEntity<String> generateOtp(@RequestBody Map<String, String> request) {
		try{
		String email = request.get("email");
		otpService.generateOtp(email);
		return ResponseEntity.status(HttpStatus.CREATED).body("OTP Generated Successfully.");
		} catch (RuntimeException r){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(r.getMessage());
		}

	}

	@PostMapping("/resend")
	public ResponseEntity<String> resendOtp(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		try {
			int resendCount = otpService.resendOtp(email);
			String message = String.format("OTP has been resent successfully. You have requested a resend %d times.", resendCount);
			return ResponseEntity.status(HttpStatus.OK).body(message);
		}
		catch (RuntimeException r) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(r.getMessage());
		}
	}

	@PostMapping("/validate")
	public ResponseEntity<String> validateOtp(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		String otpCode = request.get("otpCode");
		try {
			boolean otpValidation = otpService.validateOtp(email, otpCode);
			if (otpValidation) {
				return ResponseEntity.status(HttpStatus.OK).body("OTP is valid.");
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OTP is not valid.");
			}
		} catch (RuntimeException r) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(r.getMessage());
		}
	}

}