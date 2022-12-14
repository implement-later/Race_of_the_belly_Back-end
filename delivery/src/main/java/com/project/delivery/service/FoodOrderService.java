package com.project.delivery.service;

import com.project.delivery.dto.request.FoodOrderDetailsRequestDto;
import com.project.delivery.dto.request.FoodOrderRequestDto;
import com.project.delivery.dto.response.FoodOrderDetailsResponseDto;
import com.project.delivery.dto.response.FoodOrderResponseDto;
import com.project.delivery.dto.response.ResponseDto;
import com.project.delivery.entity.*;
import com.project.delivery.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodOrderService {
    private final CustomerRepository customerRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodOrderDetailsRepository foodOrderDetailsRepository;
    private final FoodOrderRepository foodOrderRepository;

    // Order = SQL statement --> change to OrderFood.java

    @Transactional
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

        FoodOrder foodOrder = new FoodOrder(restaurant, customer);

        ResponseDto<?> updateResponse = updateFoodOrderDetails(foodOrder, foodOrderRequestDto);

        if (updateResponse.getCode() != 200) {
            return updateResponse;
        }

        foodOrderRepository.save(foodOrder);

        return ResponseDto.success(getOrderResponse(foodOrder));
    }

    @Transactional
    public ResponseDto<?> getOrder(Long orderId, MemberDetailsImpl memberDetails) {
        // 주문 요청한 손님이랑 주문 받은 식당만 열람 가능
        FoodOrder foodOrder = foodOrderRepository.findById(orderId).orElse(null);
        if (foodOrder == null) {
            return ResponseDto.fail(404, "Not Found", "해당 주문이 존재하지 않습니다");
        }

        if (memberDetails.isCustomer()) {
            // 해당 로그인이 손님일 경우
            Customer customer = memberDetails.getCustomer();
            Customer foodOrderCustomer = customerRepository.findByUsername(foodOrder.getCustomerUsername()).orElse(null);

            if (! foodOrderCustomer.getId().equals(customer.getId())) {
                return ResponseDto.fail(403, "Forbidden Request", "주문한 손님이 아닙니다");
            }
        } else {
            // 해당 로그인이 식당일 경우
            Restaurant restaurant = memberDetails.getRestaurant();
            Restaurant foodOrderRestaurant = restaurantRepository.findByUsername(foodOrder.getRestaurantUsername()).orElse(null);

            if (! foodOrderRestaurant.getId().equals(restaurant.getId())) {
                return ResponseDto.fail(403, "Forbidden Request", "주문받은 식당이 아닙니다");
            }
        }

        return ResponseDto.success(getOrderResponse(foodOrder));
    }

    @Transactional
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
        Restaurant foodOrderRestaurant = restaurantRepository.findByUsername(foodOrder.getRestaurantUsername()).orElse(null);

        if (! foodOrderRestaurant.getId().equals(restaurant.getId())) {
            return ResponseDto.fail(403, "Forbidden Request", "주문받은 식당이 아닙니다");
        }

        foodOrder.acceptOrder();
        foodOrderRepository.save(foodOrder);

        return ResponseDto.success(getOrderResponse(foodOrder));
    }

    @Transactional
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
        Customer foodOrderCustomer = customerRepository.findByUsername(foodOrder.getCustomerUsername()).orElse(null);

        if (! foodOrderCustomer.getId().equals(customer.getId())) {
            return ResponseDto.fail(403, "Forbidden Request", "주문한 손님이 아닙니다");
        }
        if (foodOrder.isAccepted()) {
            return ResponseDto.fail(403, "Forbidden Request", "주문이 이미 수락된 상태입니다");
        }

        List<FoodOrderDetails> foodOrderDetailsList = foodOrderDetailsRepository.findByFoodOrder(foodOrder);

        // add new order details
        ResponseDto<?> updateResponse = updateFoodOrderDetails(foodOrder, foodOrderRequestDto);

        if (updateResponse.getCode() != 200) {
            return updateResponse;
        }

        // delete existing order
        for (FoodOrderDetails foodOrderDetails : foodOrderDetailsList) {
            foodOrderDetailsRepository.delete(foodOrderDetails);
        }

        return ResponseDto.success(getOrderResponse(foodOrder));
    }

    @Transactional
    public ResponseDto<?> deleteOrder(Long orderId, MemberDetailsImpl memberDetails) {
        // 주문 요청한 손님: 요천한 주문이 수락되기 전에만 취소 (삭제) 가능
        // 주문 받은 식당: 주문 거절
        FoodOrder foodOrder = foodOrderRepository.findById(orderId).orElse(null);

        if (foodOrder == null) {
            return ResponseDto.fail(404, "Not Found", "해당 주문 요청이 없습니다");
        }

        if (! memberDetails.isCustomer()) {
            Restaurant restaurant = memberDetails.getRestaurant();
            Restaurant foodOrderRestaurant = restaurantRepository.findByUsername(foodOrder.getRestaurantUsername()).orElse(null);

            if (! restaurant.getUsername().equals(foodOrderRestaurant.getUsername())) {
                return ResponseDto.fail(403, "Forbidden Request", "해당 주문을 받은 식당이 아닙니다");
            }
            if (foodOrder.isAccepted()) {
                return ResponseDto.fail(403, "Forbidden Request", "수락하신 주문은 삭제 불가합니다");
            }
        } else if (memberDetails.isCustomer()) {
            Customer customer = memberDetails.getCustomer();
            Customer foodOrderCustomer = customerRepository.findByUsername(foodOrder.getCustomerUsername()).orElse(null);

            if (! customer.getId().equals(foodOrderCustomer.getId())) {
                return ResponseDto.fail(403, "Forbidden Request", "주문을 요청한 손님만 삭제 요청 가능합니다");
            }
            // 주문요청한 손님이 맞을경우, 주문 수락 전에만 삭제 (취소) 가능
            if (foodOrder.isAccepted()) {
                return ResponseDto.fail(403, "Forbidden Request", "요청한 주문이 이미 수락 되었습니다");
            }
        }

        deleteOrder(foodOrder);

        String message = memberDetails.isCustomer() ? "주문을 취소하였습니다" : "주문을 거절하였습니다";

        return ResponseDto.success(message);
    }

    @Transactional
    public ResponseDto<?> completeOrder(Long orderId, MemberDetailsImpl memberDetails) {
        // 주문 받은 식당이 음식주문을 완료했다고 요청하는 API
        FoodOrder foodOrder = foodOrderRepository.findById(orderId).orElse(null);

        if (foodOrder == null) {
            return ResponseDto.fail(404, "Not Found", "해당 주문 요청이 없습니다");
        }

        Restaurant foodOrderRestaurant = restaurantRepository.findByUsername(foodOrder.getRestaurantUsername()).orElse(null);

        if (! memberDetails.isCustomer() && ! memberDetails.getRestaurant().getUsername().equals(foodOrderRestaurant.getUsername())) {
            return ResponseDto.fail(403, "Forbidden Request", "해당 주문을 받은 식당이 아닙니다");
        } else if (memberDetails.isCustomer()) {
            return ResponseDto.fail(403, "Forbidden Request", "손님은 주문 완료하기를 실행할수 없습니다");
        } else if (! foodOrder.isAccepted()) {
            return ResponseDto.fail(403, "Forbidden Request", "주문을 먼저 수락하셔야됩니다");
        }
        deleteOrder(foodOrder);

        return ResponseDto.success("주문이 완료 되었습니다!");
    }


    private FoodOrderResponseDto getOrderResponse(FoodOrder foodOrder) {
        // REQUIREMENT: orderFood MUST be a valid order

        List<FoodOrderDetailsResponseDto> foodOrderDetailsResponseDtoList = new ArrayList<>();
        int totalPrice = 0;

        for (FoodOrderDetails foodOrderDetails : foodOrderDetailsRepository.findByFoodOrder(foodOrder)) {
            String restaurantUsername = foodOrder.getRestaurantUsername();
            String menuName = foodOrderDetails.getMenuName();
            Menu menu = menuRepository.findByRestaurantUsernameAndMenuName(restaurantUsername, menuName).orElse(null);

            foodOrderDetailsResponseDtoList.add(new FoodOrderDetailsResponseDto(foodOrderDetails, menu));
            totalPrice += foodOrderDetails.getPrice() * foodOrderDetails.getCount();
        }
        return FoodOrderResponseDto.builder()
                .orderId(foodOrder.getId())
                .customerUsername(foodOrder.getCustomerUsername())
                .restaurantUsername(foodOrder.getRestaurantUsername())
                .foodOrderDetailsResponseDtoList(foodOrderDetailsResponseDtoList)
                .totalPrice(totalPrice)
                .accepted(foodOrder.isAccepted())
                .build();
    }

    private void deleteOrder(FoodOrder foodOrder) {
        for (FoodOrderDetails foodOrderDetails : foodOrderDetailsRepository.findByFoodOrder(foodOrder)) {
            foodOrderDetailsRepository.delete(foodOrderDetails);
        }

        foodOrderRepository.delete(foodOrder);
    }

    private ResponseDto<?> updateFoodOrderDetails(FoodOrder foodOrder, FoodOrderRequestDto foodOrderRequestDto) {
        Customer customer = customerRepository.findByUsername(foodOrder.getCustomerUsername()).orElse(null);

        Restaurant restaurant = restaurantRepository.findByUsername(foodOrder.getRestaurantUsername()).orElse(null);
        int totalPrice = 0;

        for (FoodOrderDetailsRequestDto foodOrderDetailsRequestDto : foodOrderRequestDto.getOrderDetailsList()) {
            Menu menu = menuRepository.findByRestaurantUsernameAndMenuName(restaurant.getUsername(), foodOrderDetailsRequestDto.getMenuName()).orElse(null);

            if (menu == null) {
                return ResponseDto.fail(404, "Not Found", "요청한 메뉴가 없습니다");
            }
            FoodOrderDetails foodOrderDetails = new FoodOrderDetails(foodOrder, foodOrderDetailsRequestDto.getCount(), menu.getMenuName(), menu.getPrice());

            foodOrderDetailsRepository.save(foodOrderDetails);

            totalPrice += menu.getPrice() * foodOrderDetails.getCount();
        }

        return ResponseDto.success("updated successfully");
    }
}



