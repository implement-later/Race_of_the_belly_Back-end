package com.project.delivery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESTAURANT_ID", nullable = false)
    private Restaurant restaurant;

    // TODO: Set @ManyToMany using 중간 테이블
    @ElementCollection
    @JoinColumn(nullable = false)
    private List<String> menuNameList;

    @ElementCollection
    @Column(nullable = false)
    private List<Integer> countList;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private boolean accepted;

    public void acceptOrder() {
        this.accepted = true;
    }

    public void update(List<String> menuNameList, List<Integer> countList, int totalPrice) {
        this.menuNameList = menuNameList;
        this.countList = countList;
        this.totalPrice = totalPrice;
        this.accepted = false;
    }

    // for debugging
    @Override
    public String toString() {
        return String.format("%d %s %s", this.id, this.restaurant.getUsername(), this.customer.getUsername());
    }
}
