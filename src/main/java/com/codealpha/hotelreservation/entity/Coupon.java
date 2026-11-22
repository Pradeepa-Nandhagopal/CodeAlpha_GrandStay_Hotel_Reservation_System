package com.codealpha.hotelreservation.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "coupons")
public class Coupon {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String code;
    private double discountPercent;
    private LocalDate expiryDate;
    private boolean active = true;

    public Integer getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(double discountPercent) { this.discountPercent = discountPercent; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
