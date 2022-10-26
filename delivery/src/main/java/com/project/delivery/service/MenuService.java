package com.project.delivery.service;

import com.project.delivery.dto.request.MenuRequestDto;
import com.project.delivery.dto.response.MenuResponseDto;
import com.project.delivery.dto.response.ResponseDto;
import com.project.delivery.entity.Menu;
import com.project.delivery.entity.Restaurant;
import com.project.delivery.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;


    @Transactional
    public ResponseDto<?> createMenu(MenuRequestDto menuRequestDto, MemberDetailsImpl memberDetails) {
        // 식당만 본인 식당에 메뉴 생성 요청 가능
        if (memberDetails.isCustomer()) {
            return ResponseDto.fail(403, "Forbidden Request", "식당이 아닙니다");
        }
        Restaurant restaurant = memberDetails.getRestaurant();
        if (menuRepository.existsByRestaurantAndMenuName(restaurant, menuRequestDto.getMenuName())) {
            return ResponseDto.fail(400, "Bad Request", "중복된 메뉴입니다");
        }
        Menu menu = new Menu(menuRequestDto, restaurant);
        menuRepository.save(menu);
        return ResponseDto.success(new MenuResponseDto(menu));
    }

    @Transactional
    public ResponseDto<?> getMenu(Long menuId, MemberDetailsImpl memberDetails) {
        // 손님과 해당 식당만 메뉴 조회 가능
        Menu menu = menuRepository.findById(menuId).orElse(null);

        if (menu == null) {
            return ResponseDto.fail(404, "Not Found", "조회한 메뉴가 없는 메뉴입니다");
        }

        if (! memberDetails.isCustomer() && ! memberDetails.getRestaurant().getUsername().equals(menu.getRestaurant().getUsername())) {
            return ResponseDto.fail(403, "Forbidden Request", "해당 식당이 아닙니다");
        }
        return ResponseDto.success(new MenuResponseDto(menu));
    }

    @Transactional
    public ResponseDto<?> updateMenu(Long menuId, MenuRequestDto menuRequestDto, MemberDetailsImpl memberDetails) {
        // 식당이 본인 식당 메뉴만 수정 가능

        if (memberDetails.isCustomer()) {
            return ResponseDto.fail(403, "Forbidden Request", "손님은 메뉴 수정 불가합니다");
        }

        Restaurant restaurant = memberDetails.getRestaurant();

        Menu menu = menuRepository.findById(menuId).orElse(null);
        if (menu == null) {
            return ResponseDto.fail(404, "Not Found", "수정하려는 메뉴가 없습니다");
        }
        if (! menu.getRestaurant().getUsername().equals(restaurant.getUsername())) {
            return ResponseDto.fail(403, "Forbidden Request", "본인 식당 메뉴가 아닙니다");
        }
        menu.update(menuRequestDto);

        return ResponseDto.success(new MenuResponseDto(menu));
    }

    @Transactional
    public ResponseDto<?> deleteMenu(Long menuId, MemberDetailsImpl memberDetails) {
        // 식당이 본인 식당 메뉴만 삭제 가능
        if (memberDetails.isCustomer()) {
            return ResponseDto.fail(403, "Forbidden Request", "손님은 메뉴 삭제 불가합니다");
        }
        Restaurant restaurant = memberDetails.getRestaurant();

        Menu menu = menuRepository.findById(menuId).orElse(null);
        if (menu == null) {
            return ResponseDto.fail(404, "Not Found", "수정하려는 메뉴가 없습니다");
        }
        if (! menu.getRestaurant().getUsername().equals(restaurant.getUsername())) {
            return ResponseDto.fail(403, "Forbidden Request", "본인 식당 메뉴가 아닙니다");
        }
        menuRepository.delete(menu);

        return ResponseDto.success(String.format("%s 를 성공적으로 삭제하였습니다", menu.getMenuName()));
    }
}
