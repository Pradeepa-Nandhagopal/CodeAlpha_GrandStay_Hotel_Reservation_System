
package com.codealpha.hotelreservation.repository;

import com.codealpha.hotelreservation.entity.CustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Integer> {

    Optional<CustomerAccount> findByEmailIgnoreCase(String email);
}
