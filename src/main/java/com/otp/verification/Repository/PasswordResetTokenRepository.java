package com.otp.verification.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otp.verification.Entity.PasswordResetToken;
import com.otp.verification.Entity.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	Optional<PasswordResetToken> findByToken(String token);
	Optional<PasswordResetToken> findByUser(User user);
}