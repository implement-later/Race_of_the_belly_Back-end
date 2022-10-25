package com.project.delivery.controller;


import com.project.delivery.dto.request.FoodOrderRequestDto;
import com.project.delivery.dto.response.ResponseDto;
import com.project.delivery.service.MemberDetailsImpl;
import com.project.delivery.service.FoodOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class FoodOrderController {

    private final FoodOrderService foodOrderService;

    @PostMapping("")
    public ResponseDto<?> createOrder(@RequestBody FoodOrderRequestDto foodOrderRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return foodOrderService.createOrder(foodOrderRequestDto, memberDetails);
    }

    @PostMapping("/{orderId}")
    public ResponseDto<?> acceptOrder(@PathVariable Long orderId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return foodOrderService.acceptOrder(orderId, memberDetails);
    }

    @GetMapping("/{orderId}")
    public ResponseDto<?> getOrder(@PathVariable Long orderId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return foodOrderService.getOrder(orderId, memberDetails);
    }

    @PutMapping("/{orderId}")
    public ResponseDto<?> updateOrder(@PathVariable Long orderId, @RequestBody FoodOrderRequestDto foodOrderRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return foodOrderService.updateOrder(orderId, foodOrderRequestDto, memberDetails);
    }

    @DeleteMapping("/{orderId}")
    public ResponseDto<?> deleteOrder(@PathVariable Long orderId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return foodOrderService.deleteOrder(orderId, memberDetails);
    }

    @DeleteMapping("/complete/{orderId}")
    public ResponseDto<?> completeOrder(@PathVariable Long orderId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return foodOrderService.completeOrder(orderId, memberDetails);
    }
}
