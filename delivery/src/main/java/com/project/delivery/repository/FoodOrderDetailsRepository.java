package com.project.delivery.repository;

import com.project.delivery.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodOrderDetailsRepository extends JpaRepository<FoodOrderDetails, Long> {

    List<FoodOrderDetails> findByFoodOrder(FoodOrder foodOrder);
}
