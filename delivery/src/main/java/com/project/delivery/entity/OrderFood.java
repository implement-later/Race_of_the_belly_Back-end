package com.project.delivery.entity;

import com.project.delivery.dto.request.OrderRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class OrderFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESTAURANT_ID", nullable = false)
    private Restaurant restaurant;

    @ElementCollection
    @Column(nullable = false)
    private List<String> menuNameList;

    @ElementCollection
    @Column(nullable = false)
    private List<Integer> priceList;

    @ElementCollection
    @Column(nullable = false)
    private List<Integer> countList;

    public OrderFood(Member member, Restaurant restaurant, List<Integer> priceList, OrderRequestDto orderRequestDto) {
        this.member = member;
        this.restaurant = restaurant;
        this.menuNameList = orderRequestDto.getMenuNameList();
        this.priceList = priceList;
        this.countList = orderRequestDto.getCountList();

        System.out.println(String.format("Id : %d", this.id));
    }

    // for debugging
    @Override
    public String toString() {
        return String.format("%d %s %s", this.id, this.restaurant.getUsername(), this.member.getUsername());
    }
}
