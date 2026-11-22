package com.codealpha.hotelreservation.controller;

import com.codealpha.hotelreservation.entity.Room;
import com.codealpha.hotelreservation.repository.RoomRepository;
import com.codealpha.hotelreservation.repository.BookingRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomSearchController {
    private final RoomRepository rooms;
    private final BookingRepository bookings;

    public RoomSearchController(RoomRepository rooms, BookingRepository bookings) {
        this.rooms = rooms;
        this.bookings = bookings;
    }

    @GetMapping("/search")
    public List<Room> search(
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {

        if (checkOut == null || !checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out must be after check-in.");
        }

        List<Integer> unavailableIds = bookings.findAll().stream()
                .filter(b -> "CONFIRMED".equalsIgnoreCase(b.getStatus()))
                .filter(b -> b.getCheckIn().isBefore(checkOut)
                        && b.getCheckOut().isAfter(checkIn))
                .map(b -> b.getRoom().getId())
                .distinct()
                .collect(Collectors.toList());

        return rooms.findAll().stream()
                .filter(Room::isAvailable)
                .filter(r -> roomType == null || roomType.isBlank()
                        || r.getRoomType().equalsIgnoreCase(roomType))
                .filter(r -> minPrice == null || r.getPrice() >= minPrice)
                .filter(r -> maxPrice == null || r.getPrice() <= maxPrice)
                .filter(r -> !unavailableIds.contains(r.getId()))
                .collect(Collectors.toList());
    }
}
