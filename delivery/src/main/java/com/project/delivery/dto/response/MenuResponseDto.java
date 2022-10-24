package com.project.delivery.dto.response;

import com.project.delivery.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponseDto {

    private Long menuId;
    private String menuName;
    private int price;
    public MenuResponseDto(Menu menu) {
        this.menuId = menu.getId();
        this.menuName = menu.getMenuName();
        this.price = menu.getPrice();
    }
}
