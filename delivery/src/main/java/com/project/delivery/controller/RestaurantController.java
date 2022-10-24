package com.project.delivery.controller;

import com.project.delivery.dto.response.ResponseDto;
import com.project.delivery.service.MemberDetailsImpl;
import com.project.delivery.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/restaurant-list")
    public ResponseDto<?> gerRestaurantList(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return restaurantService.getRestaurantList(memberDetails);
    }
}
