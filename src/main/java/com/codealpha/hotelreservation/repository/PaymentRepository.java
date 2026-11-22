
package com.codealpha.hotelreservation.repository;

import com.codealpha.hotelreservation.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository
        extends JpaRepository<Payment, Integer> {

    List<Payment> findByBookingId(Integer bookingId);
}
