package com.project.delivery.repository;

import com.project.delivery.entity.FoodOrder;
import com.project.delivery.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {

    List<FoodOrder> findByRestaurant(Restaurant restaurant);
}
