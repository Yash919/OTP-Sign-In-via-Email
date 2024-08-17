package com.otp.verification.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otp.verification.Entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	Optional<PasswordResetToken> findByToken(String token);
}