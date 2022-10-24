package com.project.delivery.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class OrderRequestDto {

    private String memberUsername;
    private String restaurantUsername;
    private List<OrderDetailsRequestDto> orderDetailsList;

}
