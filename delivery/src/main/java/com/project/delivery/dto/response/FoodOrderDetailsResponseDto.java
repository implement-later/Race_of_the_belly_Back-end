package com.project.delivery.dto.response;

import com.project.delivery.entity.FoodOrderDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FoodOrderDetailsResponseDto {
    private Long menuId;
    private String menuName;
    private int menuPrice;
    private int menuCount;

    public FoodOrderDetailsResponseDto(FoodOrderDetails foodOrderDetails) {
        this.menuId = foodOrderDetails.getMenu().getId();
        this.menuName = foodOrderDetails.getMenu().getMenuName();
        this.menuPrice = foodOrderDetails.getMenu().getPrice();
        this.menuCount = foodOrderDetails.getCount();
    }
}
