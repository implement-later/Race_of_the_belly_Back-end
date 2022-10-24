package com.project.delivery.service;

import com.project.delivery.dto.response.ResponseDto;
import com.project.delivery.dto.response.RestaurantResponseDto;
import com.project.delivery.entity.Restaurant;
import com.project.delivery.repository.MemberRepository;
import com.project.delivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;

    public ResponseDto<?> getRestaurantList(MemberDetailsImpl memberDetails) {

        if (memberDetails.getMember() == null) {
// 해당 로그인이 식당일 때
            return ResponseDto.fail(403, "Forbidden Request", "식당은 조회할 수 없습니다");
        } else {
// 해당 로그인이 손님일 때
            List<Restaurant> restaurants = restaurantRepository.findAll();
            List<RestaurantResponseDto> restaurantList = new ArrayList<>();

            for (Restaurant restaurant : restaurants) {
                RestaurantResponseDto restaurantListResponseDto = new RestaurantResponseDto(restaurant);
                restaurantList.add(restaurantListResponseDto);

            }
            return ResponseDto.success(restaurantList);
        }


    }
}
