package com.project.delivery.service;

import com.project.delivery.dto.request.MenuRequestDto;
import com.project.delivery.dto.response.MemberResponseDto;
import com.project.delivery.dto.response.MenuResponseDto;
import com.project.delivery.dto.response.ResponseDto;
import com.project.delivery.entity.Member;
import com.project.delivery.entity.Menu;
import com.project.delivery.entity.Restaurant;
import com.project.delivery.repository.MenuRepository;
import com.project.delivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public ResponseDto<?> createMenu(MenuRequestDto menuRequestDto, MemberDetailsImpl memberDetails) {
        // 식당만 본인 식당에 메뉴 생성 요청 가능
        Restaurant restaurant = memberDetails.getRestaurant();
        if (restaurant == null) {
            return ResponseDto.fail(400, "Bad Request", "식당이 아닙니다");
        }
        Menu menu = new Menu(menuRequestDto, restaurant.getUsername());
        menuRepository.save(menu);
        return ResponseDto.success(new MenuResponseDto(menu));
    }
    public ResponseDto<?> getMenuList(Long restaurantId, MemberDetailsImpl memberDetails) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) {
            return ResponseDto.fail(404, "Not Found", "요청한 식당이 없습니다");
        }

        Member member = memberDetails.getMember();
        Restaurant restaurantRequested = memberDetails.getRestaurant();

        if (member == null && restaurantRequested == null) {
            return ResponseDto.fail(403, "Forbidden Request", "로그인 필요합니다");
        }

        List<MenuResponseDto> menuList = new ArrayList<>();

        for (Menu menu : menuRepository.findByRestaurantUsername(restaurant.getUsername())) {
            menuList.add(new MenuResponseDto(menu));
        }

        return ResponseDto.success(menuList);
    }
}
