
package com.codealpha.hotelreservation.service;

import com.codealpha.hotelreservation.entity.Booking;
import com.codealpha.hotelreservation.entity.Payment;
import com.codealpha.hotelreservation.repository.BookingRepository;
import com.codealpha.hotelreservation.repository.PaymentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            BookingRepository bookingRepository) {

        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    // Simulate payment
    @Transactional
    public Payment processPayment(
            Integer bookingId,
            String paymentMethod,
            boolean simulateSuccess) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        if (!"CONFIRMED".equals(booking.getStatus())) {
            throw new RuntimeException(
                    "Payment not allowed for cancelled bookings");
        }

        if (!paymentMethod.equalsIgnoreCase("UPI")
                && !paymentMethod.equalsIgnoreCase("CARD")
                && !paymentMethod.equalsIgnoreCase("CASH")) {
            throw new RuntimeException("Invalid payment method");
        }

        List<Payment> existingPayments =
                paymentRepository.findByBookingId(bookingId);

        boolean alreadyPaid = existingPayments.stream()
                .anyMatch(p -> "SUCCESS".equals(p.getPaymentStatus()));

        if (alreadyPaid) {
            throw new RuntimeException("Booking already paid");
        }

        Payment payment = new Payment();

        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        payment.setPaymentMethod(paymentMethod.toUpperCase());
        payment.setPaymentStatus(
                simulateSuccess ? "SUCCESS" : "FAILED");
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    // View all payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // View payments for a booking
    public List<Payment> getBookingPayments(Integer bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }
}
