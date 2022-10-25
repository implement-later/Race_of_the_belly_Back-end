package com.project.delivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FoodOrderDetailsResponseDto {
    private Long menuId;
    private String menuName;
    private int menuPrice;
    private int menuCount;
}
