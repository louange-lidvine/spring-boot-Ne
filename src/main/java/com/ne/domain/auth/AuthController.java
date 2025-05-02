package com.ne.domain.auth;

import com.ne.domain.OTP.OtpService;
import com.ne.domain.OTP.OtpType;
import com.ne.domain.auth.dtos.*;
import com.ne.domain.commons.exceptions.BadRequestException;
import com.ne.domain.email.EmailService;
import com.ne.domain.users.UserService;
import com.ne.domain.users.dtos.UserResponseDto;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;

    @PostMapping("/register")
    @RateLimiter(name = "auth-rate-limiter")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto user, UriComponentsBuilder uriBuilder) {
        var userResponse = userService.createUser(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userResponse.id()).toUri();

        // Generate and send OTP
        var otpToSend = otpService.generateOtp(userResponse.email(), userResponse.firstName(), OtpType.VERIFY_ACCOUNT);

        emailService.sendAccountVerificationEmail(userResponse.email(), userResponse.firstName(), otpToSend);

        return ResponseEntity.created(uri).body(userResponse);
    }

    @PatchMapping("/verify-account")
    @RateLimiter(name = "auth-rate-limiter")
    public ResponseEntity<?> verifyAccount(@Valid @RequestBody VerifyAccountDto verifyAccountRequest) {
        boolean verified = otpService.verifyOtp(verifyAccountRequest.email(), verifyAccountRequest.otp(), OtpType.VERIFY_ACCOUNT);
        if (!verified) {
            throw new BadRequestException("Invalid email or OTP");
        }
        userService.activateUserAccount(verifyAccountRequest.email());
        return ResponseEntity.ok("Account Activated successfully");
    }

    @PostMapping("/initiate-password-reset")
    public ResponseEntity<?> initiatePasswordReset(@Valid @RequestBody InitiatePasswordResetDto initiateRequest) {
        var user = userService.findByEmail(initiateRequest.email());
        var otpToSend = otpService.generateOtp(user.getEmail(), user.getFirstName(), OtpType.RESET_PASSWORD);
        emailService.sendResetPasswordOtp(user.getEmail(), user.getFirstName(), otpToSend);

        return ResponseEntity.ok("If your email is registered, you will receive an email with instructions to reset your password.");
    }

    @PatchMapping("/reset-password")
    @RateLimiter(name = "auth-rate-limiter")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordRequest) {
        boolean verified = otpService.verifyOtp(resetPasswordRequest.email(), resetPasswordRequest.otp(), OtpType.RESET_PASSWORD);
        if (!verified) {
            throw new BadRequestException("Invalid email or OTP");
        }
        userService.changeUserPassword(resetPasswordRequest.email(), resetPasswordRequest.newPassword());
        return ResponseEntity.ok("Password reset went successfully. You can login with your new password.");
    }

    @PostMapping("/login")
    @RateLimiter(name = "auth-rate-limiter")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        var loginResult = authService.login(loginRequestDto, response);
        return ResponseEntity.ok(new LoginResponse(loginResult.accessToken()));
    }





}