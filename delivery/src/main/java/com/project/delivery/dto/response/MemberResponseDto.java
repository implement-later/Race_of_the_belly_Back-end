package com.project.delivery.dto.response;

import com.project.delivery.entity.Customer;
import com.project.delivery.entity.Restaurant;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    // This Dto takes both Member AND Restaurant entity
    private String name;
    private String username;
    private String userType;

    public MemberResponseDto(Customer customer) {
        this.name = customer.getName();
        this.username = customer.getUsername();
        this.userType = "Customer";
    }

    public MemberResponseDto(Restaurant restaurant) {
        this.name = restaurant.getName();
        this.username = restaurant.getUsername();
        this.userType = "Restaurant";
    }
}
