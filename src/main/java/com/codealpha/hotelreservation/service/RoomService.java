
package com.codealpha.hotelreservation.service;

import com.codealpha.hotelreservation.entity.Room;
import com.codealpha.hotelreservation.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // Display all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Display available rooms
    public List<Room> getAvailableRooms() {
        return roomRepository.findByAvailableTrue();
    }

    // Search room by ID
    public Room getRoomById(Integer id) {
        return roomRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Room not found"));
    }

    // Add a new room
    public Room addRoom(Room room) {
        return roomRepository.save(room);
    }
}
