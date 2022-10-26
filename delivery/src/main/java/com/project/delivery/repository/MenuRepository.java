package com.project.delivery.repository;

import com.project.delivery.entity.Menu;
import com.project.delivery.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Boolean existsByRestaurantAndMenuName(Restaurant restaurant, String menuName);

    List<Menu> findByRestaurant(Restaurant restaurant);

    Optional<Menu> findByRestaurantAndMenuName(Restaurant restaurant, String menuName);
}
