package com.codealpha.hotelreservation.controller;

import com.codealpha.hotelreservation.repository.RoomRepository;
import com.codealpha.hotelreservation.repository.BookingRepository;
import com.codealpha.hotelreservation.repository.CustomerRepository;
import com.codealpha.hotelreservation.repository.PaymentRepository;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    private final RoomRepository rooms;
    private final BookingRepository bookings;
    private final CustomerRepository customers;
    private final PaymentRepository payments;

    public AdminController(RoomRepository rooms, BookingRepository bookings,
                           CustomerRepository customers, PaymentRepository payments) {
        this.rooms = rooms;
        this.bookings = bookings;
        this.customers = customers;
        this.payments = payments;
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalRooms", rooms.count());
        result.put("availableRooms", rooms.findByAvailableTrue().size());
        result.put("totalBookings", bookings.count());
        result.put("totalCustomers", customers.count());
        result.put("totalPayments", payments.count());
        result.put("confirmedBookings", bookings.findByStatus("CONFIRMED").size());
        result.put("cancelledBookings", bookings.findByStatus("CANCELLED").size());
        return result;
    }
}
