package com.project.delivery.repository;

import com.project.delivery.entity.OrderFood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderFood, Long> {
}
