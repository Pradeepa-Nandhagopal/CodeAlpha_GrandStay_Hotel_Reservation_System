
package com.codealpha.hotelreservation.repository;

import com.codealpha.hotelreservation.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetOtpRepository
        extends JpaRepository<PasswordResetOtp, Integer> {

    Optional<PasswordResetOtp> findByEmail(String email);

    void deleteByEmail(String email);
}
