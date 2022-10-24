package com.project.delivery.service;

import com.project.delivery.dto.request.OrderDetailsRequestDto;
import com.project.delivery.dto.request.OrderRequestDto;
import com.project.delivery.dto.response.OrderDetailsResponseDto;
import com.project.delivery.dto.response.OrderResponseDto;
import com.project.delivery.dto.response.ResponseDto;
import com.project.delivery.entity.*;
import com.project.delivery.repository.MemberRepository;
import com.project.delivery.repository.MenuRepository;
import com.project.delivery.repository.OrderRepository;
import com.project.delivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;

    // Order = SQL statement --> change to OrderFood.java

    public ResponseDto<?> createOrder(OrderRequestDto orderRequestDto, MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();
        if (member == null) {
            return ResponseDto.fail(400, "Bad Request", "손님이 아닙니다");
        }
        Restaurant restaurant = restaurantRepository.findByUsername(orderRequestDto.getRestaurantUsername()).orElse(null);
        if (restaurant == null) {
            return ResponseDto.fail(404, "Not Found", "요청한 식당이 없습니다");
        }
        if (orderRequestDto.getOrderDetailsList().isEmpty()) {
            return ResponseDto.fail(400, "Bad Request", "비어있는 주문입니다");
        }
        List<String> menuNameList = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();
        Long totalPrice = 0L;

        // For response output
        List<OrderDetailsResponseDto> orderDetailsResponseDtoList = new ArrayList<>();

        for (OrderDetailsRequestDto orderDetailsRequestDto : orderRequestDto.getOrderDetailsList()) {
            String menuName = orderDetailsRequestDto.getMenuName();
            int count = orderDetailsRequestDto.getCount();

            Menu menu = menuRepository.findByRestaurantUsernameAndMenuName(restaurant.getUsername(), menuName).orElse(null);

            if (menu == null) {
                return ResponseDto.fail(404, "Not Found", String.format("요청한 %s 가 메뉴에 없습니다", menuName));
            }
            if (count <= 0) {
                return ResponseDto.fail(400, "Bad Request", "음식 주문 수량은 0보다 커야 됩니다");
            }

            orderDetailsResponseDtoList.add(new OrderDetailsResponseDto(menu.getId(), menu.getMenuName(), menu.getPrice(), count));

            menuNameList.add(menuName);
            countList.add(count);

            totalPrice += menu.getPrice() * count;
        }

        OrderFood orderFood = OrderFood.builder()
                .member(member)
                .restaurant(restaurant)
                .menuNameList(menuNameList)
                .countList(countList)
                .totalPrice(Long.valueOf((long) totalPrice))
                .accepted(false)
                .build();

        orderRepository.save(orderFood);

        return ResponseDto.success(OrderResponseDto.builder()
                .orderId(orderFood.getId())
                .memberUsername(member.getUsername())
                .restaurantUsername(restaurant.getUsername())
                .orderDetailsResponseDtoList(orderDetailsResponseDtoList)
                .totalPrice(totalPrice)
                .accepted(false)
                .build());
    }

    public ResponseDto<?> getOrder(Long orderId, MemberDetailsImpl memberDetails) {

        OrderFood orderFood = orderRepository.findById(orderId).orElse(null);
        if (orderFood == null) {
            return ResponseDto.fail(404, "Not Found", "해당 주문이 존재하지 않습니다");
        }
        Member member = memberDetails.getMember();
        Restaurant restaurant = memberDetails.getRestaurant();

        if (member != null && orderFood.getMember().getId() != member.getId()) {
            return ResponseDto.fail(403, "Forbidden Request", "주문한 손님이 아닙니다");
        }

        if (restaurant != null && orderFood.getRestaurant().getId() != restaurant.getId()) {
            return ResponseDto.fail(403, "Forbidden Request", "주문받은 식당이 아닙니다");
        }

        List<OrderDetailsResponseDto> orderDetailsResponseDtoList = new ArrayList<>();

        for(int i = 0; i < orderFood.getMenuNameList().size(); i++){
            Menu menu = menuRepository.findByRestaurantUsernameAndMenuName(orderFood.getRestaurant().getUsername(), orderFood.getMenuNameList().get(i)).orElse(null);
            OrderDetailsResponseDto orderDetailsResponseDto = new OrderDetailsResponseDto(menu.getId(), menu.getMenuName(), menu.getPrice(), orderFood.getCountList().get(i));
            orderDetailsResponseDtoList.add(orderDetailsResponseDto);
        }
        return ResponseDto.success(OrderResponseDto.builder()
                .orderId(orderFood.getId())
                .memberUsername(orderFood.getMember().getUsername())
                .restaurantUsername(orderFood.getRestaurant().getUsername())
                .orderDetailsResponseDtoList(orderDetailsResponseDtoList)
                .totalPrice(orderFood.getTotalPrice())
                .accepted(orderFood.isAccepted())
                .build());
    }

}



