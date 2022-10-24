package com.project.delivery.service;

import com.project.delivery.dto.request.MenuRequestDto;
import com.project.delivery.dto.response.MemberResponseDto;
import com.project.delivery.dto.response.MenuResponseDto;
import com.project.delivery.dto.response.ResponseDto;
import com.project.delivery.entity.Authority;
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

    public ResponseDto<?> getMenuList(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) {
            return ResponseDto.fail(404, "Not Found", "요청한 식당이 없습니다");
        }

        List<MenuResponseDto> menuList = new ArrayList<>();

        for (Menu menu : menuRepository.findByRestaurantUsername(restaurant.getUsername())) {
            menuList.add(new MenuResponseDto(menu));
        }

        return ResponseDto.success(menuList);
    }

    public ResponseDto<?> updateMenu(Long menuId, MenuRequestDto menuRequestDto, MemberDetailsImpl memberDetails) {
        // 식당이 본인 식당 메뉴만 수정 가능
        Restaurant restaurant = memberDetails.getRestaurant();
        if (restaurant == null) {
            return ResponseDto.fail(403, "Forbidden Request", "식당이 아닙니다");
        }

        Menu menu = menuRepository.findById(menuId).orElse(null);
        if (menu == null) {
            return ResponseDto.fail(404, "Not Found", "수정하려는 메뉴가 없습니다");
        }
        if (menu.getRestaurantUsername() != restaurant.getUsername()) {
            return ResponseDto.fail(403, "Forbidden Request", "본인 식당 메뉴가 아닙니다");
        }
        menu.update(menuRequestDto);
        menuRepository.save(menu);

        return ResponseDto.success(new MenuResponseDto(menu));
    }

    public ResponseDto<?> deleteMenu(Long menuId, MemberDetailsImpl memberDetails) {
        // 식당이 본인 식당 메뉴만 삭제 가능
        Restaurant restaurant = memberDetails.getRestaurant();
        if (restaurant == null) {
            return ResponseDto.fail(403, "Forbidden Request", "식당이 아닙니다");
        }

        Menu menu = menuRepository.findById(menuId).orElse(null);
        if (menu == null) {
            return ResponseDto.fail(404, "Not Found", "수정하려는 메뉴가 없습니다");
        }
        if (menu.getRestaurantUsername() != restaurant.getUsername()) {
            return ResponseDto.fail(403, "Forbidden Request", "본인 식당 메뉴가 아닙니다");
        }
        menuRepository.delete(menu);

        return ResponseDto.success(String.format("%s 를 성공적으로 삭제하였습니다", menu.getMenuName()));
    }
}
