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
    @JoinColumn(nullable = false)
    private List<String> menuNameList;

    @ElementCollection
    @Column(nullable = false)
    private List<Integer> countList;

    @Column(nullable = false)
    private Long totalPrice;

//    @ElementCollection
//    @Column(nullable = false)
//    private List<String> menuNameList;
//
//    @ElementCollection
//    @Column(nullable = false)
//    private List<Integer> priceList;
//
//    @ElementCollection
//    @Column(nullable = false)
//    private List<Integer> countList;

    @Column(nullable = false)
    private boolean accepted;

    public void acceptOrder() {
        this.accepted = true;
    }

    // for debugging
    @Override
    public String toString() {
        return String.format("%d %s %s", this.id, this.restaurant.getUsername(), this.member.getUsername());
    }
}