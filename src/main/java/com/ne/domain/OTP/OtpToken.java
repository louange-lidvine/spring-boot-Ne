package com.ne.domain.OTP;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email; // or phoneNumber, depending on your use case

    @Column(nullable = false)
    private String otp;


    @Column(nullable = false)
    private OtpType otpType;


    @Column(nullable = false)
    private LocalDateTime expiryTime;
}
