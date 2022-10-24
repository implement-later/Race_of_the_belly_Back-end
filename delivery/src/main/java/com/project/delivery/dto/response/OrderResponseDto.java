package com.project.delivery.dto.response;

import com.project.delivery.entity.OrderFood;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderResponseDto {


    private String memberUsername;
    private String restaurantUsername;
    private List<String> menuNameList;
    private List<Integer> priceList;
    private List<Integer> countList;
    private Long totalPrice;

    public OrderResponseDto(OrderFood orderFood){
        this.memberUsername = orderFood.getMember().getUsername();
        this.restaurantUsername = orderFood.getRestaurant().getUsername();

        this.menuNameList = new ArrayList<>();
        for (String menuName : orderFood.getMenuNameList()) {
            menuNameList.add(menuName);
        }

        this.priceList = new ArrayList<>();
        for (int price : orderFood.getPriceList()) {
            priceList.add(price);
        }

        this.countList = new ArrayList<>();
        for (int count : orderFood.getCountList()){
            this.countList.add(count);
        }
        long totPrice = (long) 0;
        for(int i = 0; i < menuNameList.size(); i++){
            totPrice += this.priceList.get(i) * this.countList.get(i);
        }
        this.totalPrice = Long.valueOf(totPrice);

    }

}
