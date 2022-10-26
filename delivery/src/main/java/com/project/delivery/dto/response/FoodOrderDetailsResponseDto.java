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

    public FoodOrderDetailsResponseDto(Long menuId, FoodOrderDetails foodOrderDetails) {
        this.menuId = menuId;
        this.menuName = foodOrderDetails.getMenuName();
        this.menuPrice = foodOrderDetails.getPrice();
        this.menuCount = foodOrderDetails.getCount();
    }
}
