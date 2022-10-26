package com.project.delivery.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class FoodOrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FoodOrder_ID", nullable = false)
    private FoodOrder foodOrder;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private int price;

    public FoodOrderDetails(FoodOrder foodOrder, int count, String menuName, int price) {
        this.foodOrder = foodOrder;
        this.count = count;
        this.menuName = menuName;
        this.price = price;
    }
}
