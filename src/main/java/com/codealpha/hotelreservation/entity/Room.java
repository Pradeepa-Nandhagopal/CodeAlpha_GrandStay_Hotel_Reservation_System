
package com.codealpha.hotelreservation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String roomType;

    @Min(1)
    private double price;

    private boolean available = true;

    // Default constructor
    public Room() {
    }

    // Parameterized constructor
    public Room(String roomType, double price) {
        this.roomType = roomType;
        this.price = price;
        this.available = true;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}

