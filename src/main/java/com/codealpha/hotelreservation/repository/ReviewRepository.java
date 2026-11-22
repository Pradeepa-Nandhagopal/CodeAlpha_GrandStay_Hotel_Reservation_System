
package com.codealpha.hotelreservation.repository;

import com.codealpha.hotelreservation.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
