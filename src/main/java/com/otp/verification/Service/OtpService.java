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


	public void generateOtp(String mailId){
		Optional<User> userOptional = userRepository.findByEmail(mailId);
		if(userOptional.isPresent()){
			User user = userOptional.get();
			String otpCode = generateRandomOtp();
			Otp otp = new Otp();
			otp.setOtp(otpCode);
			otp.setExpiryTime(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
			otp.setUser(user);

			otpRepository.save(otp);

			emailService.sendOtpEmail(user.getEmail(), otpCode);
		}
		else{
			// Runtime Exception
			throw new RuntimeException("User with email ID: " + mailId + " Not Found.");
		}
	}

	public boolean validateOtp(String mailId, String otpCode){
		Optional<User> userOptional = userRepository.findByEmail(mailId);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			Optional<Otp> otpOptional = otpRepository.findByUserAndOtp(user, otpCode);
			if(otpOptional.isPresent()){
				Otp otp = otpOptional.get();
				if (otp.getExpiryTime().isAfter(LocalDateTime.now())) {
					otpRepository.delete(otp); // OTP is valid, delete it after successful validation
					return true;
				} else {
					throw new RuntimeException("OTP has expired");
				}
			}
			else {
				throw new RuntimeException("Invalid OTP");
			}
		} else {
			throw new RuntimeException("User not found");
		}
	}

	private String generateRandomOtp(){
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		return String.valueOf(otp);
	}
}