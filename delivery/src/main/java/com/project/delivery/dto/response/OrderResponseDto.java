package com.project.delivery.dto.response;

import com.project.delivery.entity.Menu;
import com.project.delivery.entity.Order;
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
    private List<Integer> countList;
    private Long totalPrice;

    public OrderResponseDto(Order order){
        this.memberUsername = order.getMember().getUsername();
        this.restaurantUsername = order.getRestaurant().getUsername();
        this.menuNameList = new ArrayList<>();
        for( Menu menu : order.getMenuList()){
            menuNameList.add(menu.getMenuName());
        }
        this.countList = new ArrayList<>();
        for(int count : order.getCountList()){
            this.countList.add(count);
        }
        long totPrice = (long) 0;
        for(int i = 0; i < menuNameList.size(); i++){
            totPrice += order.getMenuList().get(i).getPrice() * this.countList.get(i);
        }
        this.totalPrice = Long.valueOf(totPrice);

    }

}
