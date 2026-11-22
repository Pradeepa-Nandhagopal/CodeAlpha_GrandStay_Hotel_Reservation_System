
package com.codealpha.hotelreservation.repository;

import com.codealpha.hotelreservation.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository
        extends JpaRepository<Booking, Integer> {

    List<Booking> findByCustomerId(Integer customerId);

    List<Booking> findByStatus(String status);

    List<Booking> findByRoomId(Integer roomId);
}
