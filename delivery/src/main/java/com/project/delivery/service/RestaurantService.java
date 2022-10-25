package com.project.delivery.service;

import com.project.delivery.dto.response.*;
import com.project.delivery.entity.FoodOrder;
import com.project.delivery.entity.Menu;
import com.project.delivery.entity.Restaurant;
import com.project.delivery.repository.FoodOrderRepository;
import com.project.delivery.repository.MenuRepository;
import com.project.delivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final MenuRepository menuRepository;

    public ResponseDto<?> getRestaurantList(MemberDetailsImpl memberDetails) {
        if (! memberDetails.isCustomer()) {
            // 손님만 식당 리스트 조회 가능
            return ResponseDto.fail(403, "Forbidden Request", "손님만 식당 리스트 조회 가능합니다");
        }

        List<Restaurant> restaurantList = restaurantRepository.findAll();
        List<RestaurantResponseDto> restaurantResponseDtoList = new ArrayList<>();

        for (Restaurant restaurant : restaurantList) {
            restaurantResponseDtoList.add(new RestaurantResponseDto(restaurant));
        }

        return ResponseDto.success(restaurantResponseDtoList);
    }

    public ResponseDto<?> getMenuList(Long restaurantId, MemberDetailsImpl memberDetails) {
        // 손님과 해당 식당만 식당의 메뉴리스트 조회 가능

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) {
            return ResponseDto.fail(404, "Not Found", "요청한 식당이 없습니다");
        }

        if (! memberDetails.isCustomer() && ! memberDetails.getRestaurant().getId().equals(restaurantId)) {
            return ResponseDto.fail(403, "Forbidden Request", "해당 식당 리스트를 열람할 권한이 없습니다");
        }

        List<MenuResponseDto> menuList = new ArrayList<>();

        for (Menu menu : menuRepository.findByRestaurantUsername(restaurant.getUsername())) {
            menuList.add(new MenuResponseDto(menu));
        }

        return ResponseDto.success(menuList);
    }

    public ResponseDto<?> getOrderList(Long restaurantId, MemberDetailsImpl memberDetails) {
        // 식당이 해당 식당에 요청받은 주문을 열람할수 있습니다
        if (! restaurantRepository.existsById(restaurantId)) {
            return ResponseDto.fail(404, "Not Found", "요청한 식당이 없습니다");
        }

        if (memberDetails.isCustomer()) {
            return ResponseDto.fail(403, "Forbidden Request", "식당만 요청 가능합니다");
        }
        Restaurant currentRestaurant = memberDetails.getRestaurant();

        if (! currentRestaurant.getId().equals(restaurantId)) {
            return ResponseDto.fail(403, "Forbidden Request", "해당 식당이 아닙니다");
        }

        List<FoodOrder> orderList = foodOrderRepository.findByRestaurant(currentRestaurant);

        List<FoodOrderResponseDto> fullOrderResponseList = new ArrayList<>();

        for (FoodOrder foodOrder : orderList) {
            fullOrderResponseList.add(getOrderResponse(foodOrder));
        }
        return ResponseDto.success(fullOrderResponseList);
    }

    FoodOrderResponseDto getOrderResponse(FoodOrder foodOrder) {
        // UTIL FUNCTION
        // REQUIREMENT: orderFood MUST be a valid order

        List<FoodOrderDetailsResponseDto> foodOrderDetailsResponseDtoList = new ArrayList<>();

        for(int i = 0; i < foodOrder.getMenuNameList().size(); i++){
            Menu menu = menuRepository.findByRestaurantUsernameAndMenuName(foodOrder.getRestaurant().getUsername(), foodOrder.getMenuNameList().get(i)).orElse(null);
            if (menu == null) {
                throw new NullPointerException("Menu does not exist");
            }
            FoodOrderDetailsResponseDto foodOrderDetailsResponseDto = new FoodOrderDetailsResponseDto(menu.getId(), menu.getMenuName(), menu.getPrice(), foodOrder.getCountList().get(i));
            foodOrderDetailsResponseDtoList.add(foodOrderDetailsResponseDto);
        }
        return FoodOrderResponseDto.builder()
                .orderId(foodOrder.getId())
                .memberUsername(foodOrder.getCustomer().getUsername())
                .restaurantUsername(foodOrder.getRestaurant().getUsername())
                .foodOrderDetailsResponseDtoList(foodOrderDetailsResponseDtoList)
                .totalPrice(foodOrder.getTotalPrice())
                .accepted(foodOrder.isAccepted())
                .build();
    }
}
