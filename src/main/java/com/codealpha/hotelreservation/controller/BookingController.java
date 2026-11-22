
package com.codealpha.hotelreservation.controller;

import com.codealpha.hotelreservation.entity.Booking;
import com.codealpha.hotelreservation.service.BookingService;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Book a room
    @PostMapping
    public Booking bookRoom(
            @RequestParam Integer customerId,
            @RequestParam Integer roomId,
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut) {

        return bookingService.bookRoom(
                customerId, roomId, checkIn, checkOut);
    }

    // Display all bookings
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    // Display customer bookings
    @GetMapping("/customer/{customerId}")
    public List<Booking> getCustomerBookings(
            @PathVariable Integer customerId) {

        return bookingService.getCustomerBookings(customerId);
    }

    // Cancel booking
    @PutMapping("/{bookingId}/cancel")
    public Booking cancelBooking(
            @PathVariable Integer bookingId) {

        return bookingService.cancelBooking(bookingId);
    }
}
