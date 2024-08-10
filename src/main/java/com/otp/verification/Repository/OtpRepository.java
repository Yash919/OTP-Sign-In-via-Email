package com.otp.verification.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otp.verification.Entity.Otp;
import com.otp.verification.Entity.User;

public interface OtpRepository extends JpaRepository<Otp, Long> {
	Optional<Otp> findByUserAndOtp(User user,String otp);
	Optional<Otp> findByUser(User user);
}