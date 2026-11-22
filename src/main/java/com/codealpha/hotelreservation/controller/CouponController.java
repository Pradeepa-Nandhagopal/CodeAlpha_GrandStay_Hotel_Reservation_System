package com.codealpha.hotelreservation.controller;

import com.codealpha.hotelreservation.entity.Coupon;
import com.codealpha.hotelreservation.repository.CouponRepository;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin(origins = "*")
public class CouponController {
    private final CouponRepository coupons;
    public CouponController(CouponRepository coupons) { this.coupons = coupons; }

    @PostMapping("/validate")
    public Map<String, Object> validate(@RequestBody Map<String, Object> request) {
        String code = String.valueOf(request.getOrDefault("code", "")).trim();
        double amount = Double.parseDouble(String.valueOf(request.getOrDefault("amount", "0")));
        Coupon coupon = coupons.findByCodeIgnoreCase(code)
                .filter(Coupon::isActive)
                .filter(c -> c.getExpiryDate() == null || !c.getExpiryDate().isBefore(LocalDate.now()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired coupon."));
        double discount = amount * coupon.getDiscountPercent() / 100.0;
        return Map.of("code", coupon.getCode(), "discountPercent", coupon.getDiscountPercent(),
                "discountAmount", discount, "finalAmount", amount - discount);
    }
}
