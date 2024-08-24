package com.otp.verification.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.otp.verification.Entity.Otp;
import com.otp.verification.Entity.User;
import com.otp.verification.Repository.OtpRepository;
import com.otp.verification.Repository.UserRepository;

@Service
public class OtpService {

	@Autowired
	private OtpRepository otpRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	private static final int OTP_EXPIRY_MINUTES = 5;
	private static final int MAX_RESEND_ATTEMPTS = 3;


	public void generateOtp(String mailId){
		Optional<User> userOptional = userRepository.findByEmail(mailId);
		if(userOptional.isPresent()){
			User user = userOptional.get();
			String otpCode = generateRandomOtp();
			Otp otp = new Otp();
			otp.setOtp(otpCode);
			otp.setExpiryTime(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
			otp.setUser(user);
			otp.setResendCount(0);

			otpRepository.save(otp);

			emailService.sendOtpEmail(user.getEmail(), user.getUsername(), otpCode);
		}
		else{
			throw new RuntimeException("User with email ID: " + mailId + " Not Found.");
		}
	}

	public int resendOtp(String email){
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User with email ID: " + email + " Not Found."));
		Otp otp = otpRepository.findByUser(user).orElseThrow(() -> new RuntimeException("No OTP found to resend"));

		if (otp.getResendCount() >= MAX_RESEND_ATTEMPTS) {
			throw new RuntimeException("Max OTP resend attempts reached.");
		}

		otp.setOtp(generateRandomOtp());
		otp.setExpiryTime(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
		otp.setResendCount(otp.getResendCount() + 1);

		otpRepository.save(otp);

		emailService.sendOtpEmail(user.getEmail(), user.getUsername(), otp.getOtp());

		return otp.getResendCount();
	}

	public boolean validateOtp(String email, String otpCode){
		User user = userRepository.findByEmail(email)
								  .orElseThrow(() -> new RuntimeException("User with email ID: " + email + " Not Found."));
		Otp otp = otpRepository.findByUserAndOtp(user, otpCode)
							   .orElseThrow(() -> new RuntimeException("Invalid OTP"));

		if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("OTP has expired");
		}

		otpRepository.deleteAllByUser(user); // Delete all OTPs after successful login
		return true;
	}

	private String generateRandomOtp(){
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		return String.valueOf(otp);
	}
}