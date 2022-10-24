package com.project.delivery.dto.response;

import com.project.delivery.entity.Restaurant;
import lombok.Getter;

@Getter
public class RestaurantResponseDto {

    private Long id;
    private String Restaurantname;



    public RestaurantResponseDto(Restaurant restaurant){
        this.id = restaurant.getId();
        this.Restaurantname = restaurant.getUsername();
    }
}