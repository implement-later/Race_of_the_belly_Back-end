package com.project.delivery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuRequestDto {

    private String menuName;
    private int price;

}
