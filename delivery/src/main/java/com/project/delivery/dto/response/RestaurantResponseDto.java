package com.project.delivery.dto.response;

import com.project.delivery.entity.Restaurant;
import lombok.Getter;

@Getter
public class RestaurantResponseDto {
    private String name;

    // TODO: Thumbnail Image

    public RestaurantResponseDto(Restaurant restaurant) {
        this.name = restaurant.getName();
    }
}
