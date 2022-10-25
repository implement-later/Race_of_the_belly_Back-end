package com.project.delivery.entity;

import com.project.delivery.dto.request.MenuRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @Column(nullable = false)
    private int price;

    @Column(name = "RESTAURANT", nullable = false)
    private String restaurantUsername;

    // for N:M mapping with customer
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<FoodOrderDetails> foodOrderDetailsList;

    public Menu(MenuRequestDto menuRequestDto, String restaurantUsername) {
        this.menuName = menuRequestDto.getMenuName();
        this.price = menuRequestDto.getPrice();
        this.restaurantUsername = restaurantUsername;
    }
    public void update(MenuRequestDto menuRequestDto) {
        this.menuName = menuRequestDto.getMenuName();
        this.price = menuRequestDto.getPrice();
    }
}
