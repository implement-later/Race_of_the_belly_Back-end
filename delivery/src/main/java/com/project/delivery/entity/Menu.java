package com.project.delivery.entity;

import com.project.delivery.dto.request.MenuRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"MENU_NAME", "RESTAURANT_ID"})})
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MENU_NAME", nullable = false)
    private String menuName;

    @Column(nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESTAURANT_ID", nullable = false)
    private Restaurant restaurant;

    // for N:M mapping with customer
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<FoodOrderDetails> foodOrderDetailsList;

    public Menu(MenuRequestDto menuRequestDto, Restaurant restaurant) {
        this.menuName = menuRequestDto.getMenuName();
        this.price = menuRequestDto.getPrice();
        this.restaurant = restaurant;
    }
    public void update(MenuRequestDto menuRequestDto) {
        this.menuName = menuRequestDto.getMenuName();
        this.price = menuRequestDto.getPrice();
    }
}
