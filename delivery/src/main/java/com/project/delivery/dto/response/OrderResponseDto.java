package com.project.delivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderResponseDto {

    private Long orderId;
    private String memberUsername;
    private String restaurantUsername;
    private List<OrderDetailsResponseDto> orderDetailsResponseDtoList;
    private int totalPrice;

}
