
package com.codealpha.hotelreservation.service;

import com.codealpha.hotelreservation.entity.PasswordResetOtp;
import com.codealpha.hotelreservation.repository.PasswordResetOtpRepository;
import com.codealpha.hotelreservation.repository.CustomerAccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class PasswordResetService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordResetOtpRepository otpRepository;

    @Autowired
    private CustomerAccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final SecureRandom random = new SecureRandom();

    // Generate and send OTP
    public void sendOtp(String email) {

        email = email.trim().toLowerCase();

        if (accountRepository.findByEmailIgnoreCase(email).isEmpty()) {
            throw new IllegalArgumentException(
                    "No account found with this email."
            );
        }

        // Generate 6-digit OTP
        String otp = String.format(
                "%06d",
                random.nextInt(1000000)
        );

        // Delete previous OTP
        otpRepository.deleteByEmail(email);

        // Store hashed OTP with 5-minute expiry
        PasswordResetOtp resetOtp = new PasswordResetOtp(
                email,
                passwordEncoder.encode(otp),
                LocalDateTime.now().plusMinutes(5)
        );

        otpRepository.save(resetOtp);

        // Send email
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("GrandStay - Password Reset OTP");

        message.setText(
                "Hello,\n\n" +
                        "Your GrandStay password reset OTP is: " + otp +
                        "\n\nThis OTP is valid for 5 minutes." +
                        "\n\nDo not share this OTP with anyone.\n\n" +
                        "GrandStay Hotel Reservation System"
        );

        mailSender.send(message);
    }
}
