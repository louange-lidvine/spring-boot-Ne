package com.ne.domain.users;


import com.ne.domain.OTP.OtpType;
import com.ne.domain.OTP.OtpService;

import com.ne.domain.auth.dtos.RegisterRequestDto;
import com.ne.domain.auth.dtos.VerifyAccountDto;
import com.ne.domain.auth.exceptions.InvalidOtpException;
import com.ne.domain.commons.exceptions.BadRequestException;
import com.ne.domain.commons.exceptions.NotFoundException;
import com.ne.domain.users.dtos.UserResponseDto;
import com.ne.domain.users.mappers.UserMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;

    @PostConstruct
    public void init(){
        if(!userRepository.existsByEmailOrPhoneNumberOrNationalId("admin@admin.com", "0000000000", "0000111100001111")){
            log.info("Going to set an admin.");
            var newUser = new User();
            newUser.setLastName("admin");
            newUser.setFirstName("admin");
            newUser.setEnabled(true);
            newUser.setEmail("admin@admin.com");
            newUser.setNationalId("0000111100001111");
            newUser.setPhoneNumber("0000000000");
            newUser.setVerified(true);
            newUser.setRole(Role.ADMIN);
            newUser.setPassword(passwordEncoder.encode("password"));
            userRepository.save(newUser);
        }

    }


    public UserResponseDto createUser(RegisterRequestDto user) {
        if(userRepository.existsByEmailOrPhoneNumberOrNationalId(user.email(), user.phoneNumber(), user.nationalId()))
            throw new BadRequestException("User with this email or nationalId or  phone number already exists.");

        var newUser = userMapper.toEntity(user);
        newUser.setPassword(passwordEncoder.encode(user.password()));
        newUser.setRole(Role.CUSTOMER);
        newUser.setEnabled(false);
        log.info("user is here, {}", newUser);
        userRepository.save(newUser);
        return userMapper.toResponseDto(newUser);
    }

    public void changeUserPassword(String userEmail, String newPassword){
        var user = findByEmail(userEmail);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void activateUserAccount(String userEmail){
        var user = findByEmail(userEmail);
        if (user.isEnabled())
            throw new BadRequestException("User is already activated.");

        user.setVerified(true);
        user.setEnabled(true); // if needed to allow login
        userRepository.save(user);

    }


    public void verifyAccount(VerifyAccountDto dto) {
            boolean valid = otpService.verifyOtp(dto.email(), dto.otp(), OtpType.VERIFY_ACCOUNT);
            if (!valid) throw new InvalidOtpException("Invalid OTP");

            User user = userRepository.findByEmail(dto.email())
                    .orElseThrow(() -> new NotFoundException("User not found"));

            user.setVerified(true);
            userRepository.save(user);
        }





    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User with that email not found."));
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDto)
                .toList();
    }
}
