package com.project.delivery.repository;

import com.project.delivery.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Optional<Menu> findByRestaurantUsernameAndMenuName(String restaurantUsername, String menuName);
}
