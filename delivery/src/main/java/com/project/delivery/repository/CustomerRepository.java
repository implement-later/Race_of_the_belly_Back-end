package com.project.delivery.repository;

import com.project.delivery.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByUsername(String username);
    Optional<Customer> findByUsername(String username);
}
