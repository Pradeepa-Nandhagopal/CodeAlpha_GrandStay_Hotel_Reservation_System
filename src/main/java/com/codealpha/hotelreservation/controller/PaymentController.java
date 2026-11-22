
package com.codealpha.hotelreservation.controller;

import com.codealpha.hotelreservation.entity.Payment;
import com.codealpha.hotelreservation.service.PaymentService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Process payment
    @PostMapping("/{bookingId}")
    public Payment processPayment(
            @PathVariable Integer bookingId,
            @RequestParam String paymentMethod,
            @RequestParam boolean simulateSuccess) {

        return paymentService.processPayment(
                bookingId, paymentMethod, simulateSuccess);
    }

    // Display all payments
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    // Display payments for a booking
    @GetMapping("/booking/{bookingId}")
    public List<Payment> getBookingPayments(
            @PathVariable Integer bookingId) {

        return paymentService.getBookingPayments(bookingId);
    }
}
