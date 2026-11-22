
package com.codealpha.hotelreservation.controller;

import com.codealpha.hotelreservation.entity.Customer;
import com.codealpha.hotelreservation.entity.CustomerAccount;
import com.codealpha.hotelreservation.repository.CustomerAccountRepository;
import com.codealpha.hotelreservation.repository.CustomerRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class CustomerAccountController {

    private final CustomerAccountRepository accounts;
    private final CustomerRepository customers;
    private final PasswordEncoder encoder;

    public CustomerAccountController(
            CustomerAccountRepository accounts,
            CustomerRepository customers,
            PasswordEncoder encoder) {

        this.accounts = accounts;
        this.customers = customers;
        this.encoder = encoder;
    }

    // REGISTER ACCOUNT
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestBody Map<String, String> body) {

        String email = body.getOrDefault("email", "")
                .trim().toLowerCase();

        String password = body.getOrDefault("password", "");

        if (email.isBlank() || password.length() < 8) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Enter a valid email and password with at least 8 characters."
            );
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Enter a valid email address."
            );
        }

        if (accounts.findByEmailIgnoreCase(email).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An account already exists for this email."
            );
        }

        // Find existing customer or create a new one
        Customer customer = customers.findByEmail(email)
                .orElseGet(() -> customers.save(
                        new Customer("Guest", email, "Not provided")
                ));

        // Create account
        CustomerAccount account = new CustomerAccount();
        account.setEmail(email);
        account.setPasswordHash(encoder.encode(password));
        account.setCustomerId(customer.getId());

        accounts.save(account);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "customerId", customer.getId(),
                        "email", email,
                        "message", "Account created successfully."
                )
        );
    }

    // LOGIN ACCOUNT
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> body) {

        String email = body.getOrDefault("email", "")
                .trim().toLowerCase();

        String password = body.getOrDefault("password", "");

        if (email.isBlank() || password.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email and password are required."
            );
        }

        CustomerAccount account = accounts.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password."
                ));

        if (!encoder.matches(password, account.getPasswordHash())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password."
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "customerId", account.getCustomerId(),
                        "email", account.getEmail(),
                        "message", "Login successful."
                )
        );
    }
}
