package com.project.delivery.service;

import com.project.delivery.dto.request.FoodOrderDetailsRequestDto;
import com.project.delivery.dto.request.FoodOrderRequestDto;
import com.project.delivery.dto.response.FoodOrderDetailsResponseDto;
import com.project.delivery.dto.response.FoodOrderResponseDto;
import com.project.delivery.dto.response.ResponseDto;
import com.project.delivery.entity.Customer;
import com.project.delivery.entity.Menu;
import com.project.delivery.entity.FoodOrder;
import com.project.delivery.entity.Restaurant;
import com.project.delivery.repository.MenuRepository;
import com.project.delivery.repository.FoodOrderRepository;
import com.project.delivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodOrderService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodOrderRepository foodOrderRepository;

    // Order = SQL statement --> change to OrderFood.java

    public ResponseDto<?> createOrder(FoodOrderRequestDto foodOrderRequestDto, MemberDetailsImpl memberDetails) {
        // 손님만 주문 생성 가능
        if (! memberDetails.isCustomer()) {
            return ResponseDto.fail(400, "Bad Request", "손님이 아닙니다");
        }
        Customer customer = memberDetails.getCustomer();
        Restaurant restaurant = restaurantRepository.findByUsername(foodOrderRequestDto.getRestaurantUsername()).orElse(null);
        if (restaurant == null) {
            return ResponseDto.fail(404, "Not Found", "요청한 식당이 없습니다");
        }
        if (foodOrderRequestDto.getOrderDetailsList().isEmpty()) {
            return ResponseDto.fail(400, "Bad Request", "비어있는 주문입니다");
        }

        // TODO: After setting up @ManyToMany, update this code --> can be simplified
        List<String> menuNameList = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();

        int totalPrice = 0;

        // For response output
        List<FoodOrderDetailsResponseDto> foodOrderDetailsResponseDtoList = new ArrayList<>();

        for (FoodOrderDetailsRequestDto foodOrderDetailsRequestDto : foodOrderRequestDto.getOrderDetailsList()) {
            String menuName = foodOrderDetailsRequestDto.getMenuName();
            int count = foodOrderDetailsRequestDto.getCount();

            Menu menu = menuRepository.findByRestaurantUsernameAndMenuName(restaurant.getUsername(), menuName).orElse(null);

            if (menu == null) {
                return ResponseDto.fail(404, "Not Found", String.format("요청한 %s 가 메뉴에 없습니다", menuName));
            }
            if (count <= 0) {
                return ResponseDto.fail(400, "Bad Request", "음식 주문 수량은 0보다 커야 됩니다");
            }

            foodOrderDetailsResponseDtoList.add(new FoodOrderDetailsResponseDto(menu.getId(), menu.getMenuName(), menu.getPrice(), count));

            menuNameList.add(menuName);
            countList.add(count);

            totalPrice += menu.getPrice() * count;
        }

        FoodOrder foodOrder = FoodOrder.builder()
                .customer(customer)
                .restaurant(restaurant)
                .menuNameList(menuNameList)
                .countList(countList)
                .totalPrice(totalPrice)
                .accepted(false)
                .build();

        foodOrderRepository.save(foodOrder);

        return ResponseDto.success(FoodOrderResponseDto.builder()
                .orderId(foodOrder.getId())
                .memberUsername(customer.getUsername())
                .restaurantUsername(restaurant.getUsername())
                .foodOrderDetailsResponseDtoList(foodOrderDetailsResponseDtoList)
                .totalPrice(totalPrice)
                .accepted(false)
                .build());
    }

    public ResponseDto<?> getOrder(Long orderId, MemberDetailsImpl memberDetails) {
        // 주문 요청한 손님이랑 주문 받은 식당만 열람 가능
        FoodOrder foodOrder = foodOrderRepository.findById(orderId).orElse(null);
        if (foodOrder == null) {
            return ResponseDto.fail(404, "Not Found", "해당 주문이 존재하지 않습니다");
        }

        if (memberDetails.isCustomer()) {
            // 해당 로그인이 손님일 경우
            Customer customer = memberDetails.getCustomer();
            if (! foodOrder.getCustomer().getId().equals(customer.getId())) {
                return ResponseDto.fail(403, "Forbidden Request", "주문한 손님이 아닙니다");
            }
        } else {
            // 해당 로그인이 식당일 경우
            Restaurant restaurant = memberDetails.getRestaurant();
            if (! foodOrder.getRestaurant().getId().equals(restaurant.getId())) {
                return ResponseDto.fail(403, "Forbidden Request", "주문받은 식당이 아닙니다");
            }
        }

        return ResponseDto.success(getOrderResponse(foodOrder));
    }
    public ResponseDto<?> acceptOrder(Long orderId, MemberDetailsImpl memberDetails) {
        // 주문 받은 식당만 수락 가능
        FoodOrder foodOrder = foodOrderRepository.findById(orderId).orElse(null);
        if (foodOrder == null) {
            return ResponseDto.fail(404, "Not Found", "해당 주문이 존재하지 않습니다");
        }

        if (memberDetails.isCustomer()) {
            return ResponseDto.fail(403, "Forbidden Request", "현재 로그인이 식당이 아닙니다");
        }
        Restaurant restaurant = memberDetails.getRestaurant();
        if (! foodOrder.getRestaurant().getId().equals(restaurant.getId())) {
            return ResponseDto.fail(403, "Forbidden Request", "주문받은 식당이 아닙니다");
        }

        foodOrder.acceptOrder();
        foodOrderRepository.save(foodOrder);

        return ResponseDto.success(getOrderResponse(foodOrder));
    }

    public ResponseDto<?> updateOrder(Long orderId, FoodOrderRequestDto foodOrderRequestDto, MemberDetailsImpl memberDetails) {
        // 주문 요청한 손님만 주문 수정 가능합니다

        FoodOrder foodOrder = foodOrderRepository.findById(orderId).orElse(null);

        if (foodOrder == null) {
            return ResponseDto.fail(404, "Not Found", "수정할 주문이 없는 주문입니다");
        }

        if (! memberDetails.isCustomer()) {
            return ResponseDto.fail(403, "Forbidden Request", "현재 로그인이 손님이 아닙니다");
        }
        Customer customer = memberDetails.getCustomer();
        if (! foodOrder.getCustomer().getId().equals(customer.getId())) {
            return ResponseDto.fail(403, "Forbidden Request", "주문한 손님이 아닙니다");
        }

        // TODO: Once @ManyToMany table is established, simplify this code
        List<String> menuNameList = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();
        int totalPrice = 0;

        for (FoodOrderDetailsRequestDto foodOrderDetailsRequestDto : foodOrderRequestDto.getOrderDetailsList()) {
            Menu menu = menuRepository.findByRestaurantUsernameAndMenuName(foodOrderRequestDto.getRestaurantUsername(),
                    foodOrderDetailsRequestDto.getMenuName()).orElse(null);
            if (menu == null) {
                return ResponseDto.fail(404, "Not Found", "수정할 주문이 없는 주문입니다");
            }
            menuNameList.add(menu.getMenuName());
            countList.add(foodOrderDetailsRequestDto.getCount());
            totalPrice += menu.getPrice() * foodOrderDetailsRequestDto.getCount();
        }
        foodOrder.update(menuNameList, countList, totalPrice);
        foodOrderRepository.save(foodOrder);

        return ResponseDto.success(getOrderResponse(foodOrder));
    }

    public ResponseDto<?> deleteOrder(Long orderId, MemberDetailsImpl memberDetails) {
        // 주문 요청한 손님: 요천한 주문이 수락되기 전에만 취소 (삭제) 가능
        // 주문 받은 식당: 주문 거절
        FoodOrder foodOrder = foodOrderRepository.findById(orderId).orElse(null);

        if (foodOrder == null) {
            return ResponseDto.fail(404, "Not Found", "해당 주문 요청이 없습니다");
        }

        if (! memberDetails.isCustomer()) {
            if (! memberDetails.getRestaurant().getUsername().equals(foodOrder.getRestaurant().getUsername())) {
                return ResponseDto.fail(403, "Forbidden Request", "해당 주문을 받은 식당이 아닙니다");
            }
            if (foodOrder.isAccepted()) {
                return ResponseDto.fail(403, "Forbidden Request", "수락하신 주문은 삭제 불가합니다");
            }
        } else if (memberDetails.isCustomer()) {
            Customer customer = memberDetails.getCustomer();
            if (!customer.getId().equals(foodOrder.getCustomer().getId())) {
                return ResponseDto.fail(403, "Forbidden Request", "주문을 요청한 손님만 삭제 요청 가능합니다");
            }
            // 주문요청한 손님이 맞을경우, 주문 수락 전에만 삭제 (취소) 가능
            if (foodOrder.isAccepted()) {
                return ResponseDto.fail(403, "Forbidden Request", "요청한 주문이 이미 수락 되었습니다");
            }
        }

        foodOrderRepository.delete(foodOrder);

        String message = memberDetails.isCustomer() ? "주문을 취소하였습니다" : "주문을 거절하였습니다";

        return ResponseDto.success(message);
    }

    public ResponseDto<?> completeOrder(Long orderId, MemberDetailsImpl memberDetails) {
        // 주문 받은 식당이 음식주문을 완료했다고 요청하는 API
        FoodOrder foodOrder = foodOrderRepository.findById(orderId).orElse(null);

        if (foodOrder == null) {
            return ResponseDto.fail(404, "Not Found", "해당 주문 요청이 없습니다");
        }

        if (! memberDetails.isCustomer() && ! memberDetails.getRestaurant().getUsername().equals(foodOrder.getRestaurant().getUsername())) {
            return ResponseDto.fail(403, "Forbidden Request", "해당 주문을 받은 식당이 아닙니다");
        } else if (memberDetails.isCustomer()) {
            return ResponseDto.fail(403, "Forbidden Request", "손님은 주문 완료하기를 실행할수 없습니다");
        } else if (! foodOrder.isAccepted()) {
            return ResponseDto.fail(403, "Forbidden Request", "주문을 먼저 수락하셔야됩니다");
        }

        foodOrderRepository.delete(foodOrder);

        return ResponseDto.success("주문이 완료 되었습니다!");
    }
    FoodOrderResponseDto getOrderResponse(FoodOrder foodOrder) {
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



