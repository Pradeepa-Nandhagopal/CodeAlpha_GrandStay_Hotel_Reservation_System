
package com.codealpha.hotelreservation.controller;

import com.codealpha.hotelreservation.entity.Customer;
import com.codealpha.hotelreservation.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Register customer
    @PostMapping
    public Customer registerCustomer(@RequestBody Customer customer) {
        return customerService.registerCustomer(customer);
    }

    // Display all customers
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }


    // Search customer by ID
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Integer id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(
            @PathVariable String email) {

        return customerService.findCustomerByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
