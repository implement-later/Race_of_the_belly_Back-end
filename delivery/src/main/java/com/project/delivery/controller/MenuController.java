package com.project.delivery.controller;

import com.project.delivery.dto.request.MenuRequestDto;
import com.project.delivery.dto.response.ResponseDto;
import com.project.delivery.service.MemberDetailsImpl;
import com.project.delivery.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    @PostMapping("")
    public ResponseDto<?> createMenu(@RequestBody MenuRequestDto menuRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return menuService.createMenu(menuRequestDto, memberDetails);
    }

    @GetMapping("/{restaurantId}")
    public ResponseDto<?> getMenuList(@PathVariable Long restaurantId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return menuService.getMenuList(restaurantId, memberDetails);
    }
}
