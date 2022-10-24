package com.project.delivery.dto.response;

import com.project.delivery.entity.Menu;
import com.project.delivery.entity.OrderFood;
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
    private Long totalPrice;
    private boolean accepted;

}