package com.project.delivery.repository;

import com.project.delivery.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    boolean existsByUsername(String username);
    Optional<Restaurant> findByUsername(String username);

    Optional<Restaurant> findByName(String name);
}
