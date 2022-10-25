package com.project.delivery.controller;

import com.project.delivery.dto.response.ResponseDto;
import com.project.delivery.service.FoodOrderService;
import com.project.delivery.service.MemberDetailsImpl;
import com.project.delivery.service.MenuService;

import com.project.delivery.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("")
    public ResponseDto<?> getRestaurantList(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return restaurantService.getRestaurantList(memberDetails);
    }

    @GetMapping("/{restaurantId}")
    public ResponseDto<?> getMenuList(@PathVariable Long restaurantId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return restaurantService.getMenuList(restaurantId, memberDetails);
    }

    @GetMapping("/order/{restaurantId}")
    public ResponseDto<?> getOrderList(@PathVariable Long restaurantId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return restaurantService.getOrderList(restaurantId, memberDetails);
    }
}
