package com.project.delivery.dto.request;

import com.project.delivery.entity.FoodOrderDetails;
import lombok.Getter;

@Getter
public class FoodOrderDetailsRequestDto {
    private String menuName;
    private int count;
}
