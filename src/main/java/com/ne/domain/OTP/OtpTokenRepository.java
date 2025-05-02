package com.ne.domain.OTP;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByEmailAndOtpAndOtpType(String email, String otp, OtpType otpType);
    void deleteByEmailAndOtpType(String email, OtpType otpType);
}

