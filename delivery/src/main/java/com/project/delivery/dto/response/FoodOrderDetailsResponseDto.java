package com.project.delivery.dto.response;

import com.project.delivery.entity.FoodOrderDetails;
import com.project.delivery.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FoodOrderDetailsResponseDto {
    private Long menuId;
    private String menuName;
    private int menuPrice;
    private int menuCount;

    public FoodOrderDetailsResponseDto(FoodOrderDetails foodOrderDetails, Menu menu) {
        this.menuId = menu.getId();
        this.menuName = foodOrderDetails.getMenuName();
        this.menuPrice = foodOrderDetails.getPrice();
        this.menuCount = foodOrderDetails.getCount();
    }
}
