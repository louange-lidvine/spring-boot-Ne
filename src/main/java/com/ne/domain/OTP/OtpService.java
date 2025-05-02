package com.ne.domain.OTP;

import com.ne.domain.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpTokenRepository otpTokenRepository;
    private final EmailService emailService;

    public String generateOtp(String email, String name, OtpType otpType) {
        // Generate a 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);

        // Remove existing OTPs for the email and type
        otpTokenRepository.deleteByEmailAndOtpType(email, otpType);

        // Save new OTP
        OtpToken otpToken = OtpToken.builder()
                .email(email)
                .otp(otp)
                .otpType(otpType)
                .expiryTime(expiryTime)
                .build();
        otpTokenRepository.save(otpToken);

        // Send OTP via email
        switch (otpType) {
            case VERIFY_ACCOUNT:
                emailService.sendAccountVerificationEmail(email, name, otp);
                break;
            case RESET_PASSWORD:
                emailService.sendResetPasswordOtp(email, name, otp);
                break;
            default:
                throw new IllegalArgumentException("Unsupported OTP type");
        }

        return otp;
    }

    public boolean verifyOtp(String email, String otp, OtpType otpType) {
        Optional<OtpToken> otpTokenOptional = otpTokenRepository.findByEmailAndOtpAndOtpType(email, otp, otpType);
        if (otpTokenOptional.isPresent()) {
            OtpToken otpToken = otpTokenOptional.get();
            if (otpToken.getExpiryTime().isAfter(LocalDateTime.now())) {
                otpTokenRepository.delete(otpToken); // Invalidate OTP after successful verification
                return true;
            } else {
                otpTokenRepository.delete(otpToken); // Invalidate expired OTP
            }
        }
        return false;
    }
}
