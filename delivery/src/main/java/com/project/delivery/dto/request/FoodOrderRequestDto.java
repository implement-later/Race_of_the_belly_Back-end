package com.project.delivery.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class FoodOrderRequestDto {

    private String memberUsername;
    private String restaurantUsername;
    private List<FoodOrderDetailsRequestDto> orderDetailsList;

}
