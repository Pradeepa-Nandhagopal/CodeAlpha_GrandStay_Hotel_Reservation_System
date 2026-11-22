
package com.codealpha.hotelreservation.service;

import com.codealpha.hotelreservation.entity.Booking;
import com.codealpha.hotelreservation.entity.Customer;
import com.codealpha.hotelreservation.entity.Room;
import com.codealpha.hotelreservation.repository.BookingRepository;
import com.codealpha.hotelreservation.repository.CustomerRepository;
import com.codealpha.hotelreservation.repository.RoomRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;

    public BookingService(
            BookingRepository bookingRepository,
            CustomerRepository customerRepository,
            RoomRepository roomRepository) {

        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
    }

    // Book a room
    @Transactional
    public Booking bookRoom(
            Integer customerId,
            Integer roomId,
            LocalDate checkIn,
            LocalDate checkOut) {

        // Validate dates
        if (checkIn == null || checkOut == null ||
                !checkIn.isAfter(LocalDate.now()) ||
                !checkOut.isAfter(checkIn)) {

            throw new RuntimeException("Invalid booking dates");
        }

        // Find customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));

        // Find room
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new RuntimeException("Room not found"));

        if (!room.isAvailable()) {
            throw new RuntimeException("Room is unavailable");
        }

        // Check overlapping confirmed bookings
        List<Booking> existingBookings =
                bookingRepository.findByRoomId(roomId);

        for (Booking existing : existingBookings) {

            if ("CONFIRMED".equals(existing.getStatus())) {

                boolean overlaps =
                        checkIn.isBefore(existing.getCheckOut()) &&
                                checkOut.isAfter(existing.getCheckIn());

                if (overlaps) {
                    throw new RuntimeException(
                            "Room is already booked for these dates");
                }
            }
        }

        // Calculate total amount
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        double total = nights * room.getPrice();

        // Create booking
        Booking booking = new Booking();

        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckIn(checkIn);
        booking.setCheckOut(checkOut);
        booking.setTotalAmount(total);
        booking.setStatus("CONFIRMED");

        return bookingRepository.save(booking);
    }

    // Display all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Display bookings of a customer
    public List<Booking> getCustomerBookings(Integer customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    // Cancel booking
    @Transactional
    public Booking cancelBooking(Integer bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException("Booking already cancelled");
        }

        booking.setStatus("CANCELLED");

        return bookingRepository.save(booking);
    }
}
