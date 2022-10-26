package com.project.delivery.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class FoodOrderDetails {
    /*
    This is bridging entity that briges N:1 relationship with customer and menu
    This is equivalent to N:M mapping b/w customer and menu
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "food_order_id")
    private FoodOrder foodOrder;

    private int count;

    public FoodOrderDetails(Customer customer, Restaurant restaurant, Menu menu, FoodOrder foodOrder, int count) {
        this.customer = customer;
        this.restaurant = restaurant;
        this.menu = menu;
        this.foodOrder = foodOrder;
        this.count = count;
    }
}
