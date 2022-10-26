package com.project.delivery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String restaurantUsername;

    @Column(nullable = false)
    private String customerUsername;

    @Column(nullable = false)
    private boolean accepted;

    public FoodOrder(Restaurant restaurant, Customer customer) {
        this.restaurantUsername = restaurant.getUsername();
        this.customerUsername = customer.getUsername();
        this.accepted = false;
    }


    public void acceptOrder() {
        this.accepted = true;
    }

    public void update() {
        // TODO: implement
    }
}
