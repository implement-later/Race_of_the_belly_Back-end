package com.project.delivery.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"MENU_NAME", "RESTAURANT"})})
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MENU_NAME", nullable = false)
    private String menuName;

    @Column(name = "PRICE", nullable = false)
    private int price;

    @Column(name = "RESTAURANT", nullable = false)
    private String restaurantUsername;
}
