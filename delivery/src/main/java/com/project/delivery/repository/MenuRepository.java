package com.project.delivery.repository;

import com.project.delivery.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Boolean existsByRestaurantUsernameAndMenuName(String restaurantUsername, String menuName);

    List<Menu> findByRestaurantUsername(String restaurantUsername);

    Optional<Menu> findByRestaurantUsernameAndMenuName(String restaurantUsername, String menuName);
}
