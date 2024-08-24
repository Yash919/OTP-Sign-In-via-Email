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

			Optional<Otp> existingOtpOptional = otpRepository.findByUser(user);
			if (existingOtpOptional.isPresent()) {
				Otp existingOtp = existingOtpOptional.get();

				// Check if the resendCount is 3 and the OTP has expired
				if (existingOtp.getResendCount() == 3) {
					throw new RuntimeException("Maximum OTP resend attempts reached. Please try again tomorrow.");
				}
			}

			// Create and save a new OTP
			Otp newOtp = new Otp();
			newOtp.setOtp(generateRandomOtp());
			newOtp.setExpiryTime(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
			newOtp.setUser(user);
			newOtp.setResendCount(0);
			otpRepository.save(newOtp);
			emailService.sendOtpEmail(user.getEmail(), user.getUsername(), newOtp.getOtp());

		} else {
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