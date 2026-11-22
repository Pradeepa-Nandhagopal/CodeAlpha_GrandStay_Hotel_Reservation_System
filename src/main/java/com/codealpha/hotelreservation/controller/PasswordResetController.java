
package com.codealpha.hotelreservation.controller;

import com.codealpha.hotelreservation.entity.PasswordResetOtp;
import com.codealpha.hotelreservation.entity.CustomerAccount;

import com.codealpha.hotelreservation.repository.PasswordResetOtpRepository;
import com.codealpha.hotelreservation.repository.CustomerAccountRepository;

import com.codealpha.hotelreservation.service.PasswordResetService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/password")
@CrossOrigin(origins = "*")
public class PasswordResetController {

    private final PasswordResetService resetService;
    private final PasswordResetOtpRepository otpRepository;
    private final CustomerAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetController(
            PasswordResetService resetService,
            PasswordResetOtpRepository otpRepository,
            CustomerAccountRepository accountRepository,
            PasswordEncoder passwordEncoder) {

        this.resetService = resetService;
        this.otpRepository = otpRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // SEND OTP
    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(
            @RequestBody Map<String, String> body) {

        String email = body.getOrDefault("email", "")
                .trim().toLowerCase();

        if (email.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email is required."
            );
        }

        resetService.sendOtp(email);

        return ResponseEntity.ok(
                Map.of("message", "OTP sent to your email.")
        );
    }

    // VERIFY OTP AND RESET PASSWORD
    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetPassword(
            @RequestBody Map<String, String> body) {

        String email = body.getOrDefault("email", "")
                .trim().toLowerCase();

        String otp = body.getOrDefault("otp", "").trim();

        String newPassword = body.getOrDefault("newPassword", "");

        if (email.isBlank() || otp.isBlank() ||
                newPassword.length() < 8) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Enter email, OTP and a password with at least 8 characters."
            );
        }

        // Find stored OTP
        PasswordResetOtp resetOtp = otpRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "OTP not found. Please request a new OTP."
                ));

        // Check expiry
        if (resetOtp.getExpiryTime().isBefore(LocalDateTime.now())) {

            otpRepository.deleteByEmail(email);

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "OTP expired. Please request a new OTP."
            );
        }

        // Verify OTP
        if (!passwordEncoder.matches(otp, resetOtp.getOtp())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid OTP."
            );
        }

        // Find account
        CustomerAccount account = accountRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account not found."
                ));

        // Update password
        account.setPasswordHash(
                passwordEncoder.encode(newPassword)
        );

        accountRepository.save(account);

        // Delete used OTP
        otpRepository.deleteByEmail(email);

        return ResponseEntity.ok(
                Map.of("message", "Password reset successfully.")
        );
    }
}
