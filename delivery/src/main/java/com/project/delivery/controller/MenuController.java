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

    @GetMapping("/{menuId}")
    public ResponseDto<?> getMenu(@PathVariable Long menuId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return menuService.getMenu(menuId, memberDetails);
    }

    @PutMapping("/{menuId}")
    public ResponseDto<?> updateMenu(@PathVariable Long menuId, @RequestBody MenuRequestDto menuRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return menuService.updateMenu(menuId, menuRequestDto, memberDetails);
    }

    @DeleteMapping("/{menuId}")
    public ResponseDto<?> deleteMenu(@PathVariable Long menuId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return menuService.deleteMenu(menuId, memberDetails);
    }

}
