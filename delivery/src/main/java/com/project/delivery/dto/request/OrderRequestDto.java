package com.project.delivery.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderRequestDto {
    private String customerUsername;
    private String restaurantUsername;

}
