package com.project.delivery.entity;


import com.project.delivery.dto.request.OrderRequestDto;
import com.project.delivery.util.TimeStamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends TimeStamped {
// TODO: jpa column name

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESTAURANT_ID")
    private Restaurant restaurant;


    @ManyToMany(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private List<Menu> menuList;

    @ElementCollection
    @Column(nullable = false)
    private List<Integer> countList;






}
