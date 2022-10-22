package com.project.delivery.controller;


import com.project.delivery.dto.request.OrderRequestDto;
import com.project.delivery.dto.response.ResponseDto;
import com.project.delivery.service.MemberDetailsImpl;
import com.project.delivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("")
    public ResponseDto<?> createOrder(@RequestBody OrderRequestDto orderRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return orderService.createOrder(orderRequestDto, memberDetails);
    }


   // @GetMapping("/{orderId}")



   // @PutMapping("/{orderId}")

}
