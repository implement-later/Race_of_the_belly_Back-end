package com.project.delivery.entity;

import com.project.delivery.dto.request.MemberRequestDto;
import com.project.delivery.util.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Member extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private Authority authority = Authority.ROLE_CUSTOMER;

    public Member(MemberRequestDto requestDto, PasswordEncoder passwordEncoder) {
        this.name = requestDto.getName();
        this.username = requestDto.getUsername();
        this.password = passwordEncoder.encode(requestDto.getPassword());
    }
}
