
package com.codealpha.hotelreservation.controller;

import com.codealpha.hotelreservation.entity.Booking;
import com.codealpha.hotelreservation.entity.Review;
import com.codealpha.hotelreservation.repository.BookingRepository;
import com.codealpha.hotelreservation.repository.ReviewRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewRepository reviews;
    private final BookingRepository bookings;

    public ReviewController(ReviewRepository reviews,
                            BookingRepository bookings) {
        this.reviews = reviews;
        this.bookings = bookings;
    }

    @GetMapping
    public List<Review> all() {
        return reviews.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Review review) {

        try {
            if (review.getRating() < 1 || review.getRating() > 5) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error",
                                "Rating must be between 1 and 5."));
            }

            if (review.getBookingId() == null ||
                    review.getCustomerId() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error",
                                "Customer ID and Booking ID are required."));
            }

            Booking booking = bookings.findById(review.getBookingId())
                    .orElse(null);

            if (booking == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Booking not found."));
            }

            if (booking.getCustomer() == null ||
                    !booking.getCustomer().getId()
                            .equals(review.getCustomerId())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error",
                                "Booking does not belong to this customer."));
            }

            if (!"CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error",
                                "Only confirmed bookings can be reviewed."));
            }

            Review savedReview = reviews.save(review);

            return ResponseEntity.ok(savedReview);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error",
                            "Unable to save review. Check the backend terminal."));
        }
    }
}
