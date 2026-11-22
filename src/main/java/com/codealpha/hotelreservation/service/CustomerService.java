
package com.codealpha.hotelreservation.service;

import com.codealpha.hotelreservation.entity.Customer;
import com.codealpha.hotelreservation.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Register a new customer
    public Customer registerCustomer(Customer customer) {

        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        return customerRepository.save(customer);
    }



    // Display all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Find customer by ID
    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));
    }
    public Optional<Customer> findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

}
