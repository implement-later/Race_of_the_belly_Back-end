package com.project.delivery.service;

import com.project.delivery.dto.request.OrderRequestDto;
import com.project.delivery.dto.response.OrderResponseDto;
import com.project.delivery.dto.response.ResponseDto;
import com.project.delivery.entity.Member;
import com.project.delivery.entity.Menu;
import com.project.delivery.entity.OrderFood;
import com.project.delivery.entity.Restaurant;
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

    public ResponseDto<?> createOrder(OrderRequestDto orderRequestDto, MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();
        if (member == null) {
            return ResponseDto.fail(400, "Bad Request", "손님이 아닙니다");
        }
        Restaurant restaurant = restaurantRepository.findByUsername(orderRequestDto.getRestaurantUsername()).orElse(null);
        if (restaurant == null) {
            return ResponseDto.fail(404, "Not Found", "요청한 식당이 없습니다");
        }
        if (orderRequestDto.getMenuNameList().size() != orderRequestDto.getCountList().size()) {
            return ResponseDto.fail(400, "Bad Request", "메뉴와 갯수가 일치하지 않습니다");
        }
        if (orderRequestDto.getMenuNameList().isEmpty()) {
            return ResponseDto.fail(400, "Bad Request", "비어있는 주문입니다");
        }
        List<Integer> priceList = new ArrayList<>();
        for (String menuName : orderRequestDto.getMenuNameList()) {
            Menu menu = menuRepository.findByRestaurantUsernameAndMenuName(orderRequestDto.getRestaurantUsername(), menuName).orElse(null);
            if (menu == null) {
                return ResponseDto.fail(404, "Not Found", "요청한 메뉴가 없습니다");
            }
            priceList.add(menu.getPrice());
        }
//        Order order = Order.builder()
//                .member(member)
//                .restaurant(restaurant)
//                .menuNameList(orderRequestDto.getMenuNameList())
//                .priceList(priceList)
//                .countList(orderRequestDto.getCountList())
//                .build();

        OrderFood orderFood = new OrderFood(member, restaurant, priceList, orderRequestDto);

        System.out.println(orderFood);

        orderRepository.save(orderFood);
        return ResponseDto.success(new OrderResponseDto(orderFood));
    }

}
