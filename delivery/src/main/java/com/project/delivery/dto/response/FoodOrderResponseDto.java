package com.project.delivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FoodOrderResponseDto {

    private Long orderId;
    private String memberUsername;
    private String restaurantUsername;
    private List<FoodOrderDetailsResponseDto> foodOrderDetailsResponseDtoList;

    private int totalPrice;
    private boolean accepted;

}
