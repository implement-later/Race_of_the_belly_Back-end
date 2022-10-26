package com.project.delivery.dto.response;

import com.project.delivery.entity.Restaurant;
import lombok.Getter;

@Getter
public class RestaurantResponseDto {

    private Long restaurantId;
    private String name;
    private String username;

    // TODO: Thumbnail Image  
    public RestaurantResponseDto(Restaurant restaurant) {
        this.restaurantId = restaurant.getId();
        this.name = restaurant.getName();
        this.username = restaurant.getUsername();
    }
}
