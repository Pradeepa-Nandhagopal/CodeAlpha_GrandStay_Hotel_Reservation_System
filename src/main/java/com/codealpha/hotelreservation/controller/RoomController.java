
package com.codealpha.hotelreservation.controller;

import com.codealpha.hotelreservation.entity.Room;
import com.codealpha.hotelreservation.service.RoomService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Display all rooms
    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    // Display available rooms
    @GetMapping("/available")
    public List<Room> getAvailableRooms() {
        return roomService.getAvailableRooms();
    }

    // Search room by ID
    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Integer id) {
        return roomService.getRoomById(id);
    }

    // Add a new room
    @PostMapping
    public Room addRoom(@RequestBody Room room) {
        return roomService.addRoom(room);
    }
}


