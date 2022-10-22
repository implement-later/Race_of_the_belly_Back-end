package com.project.delivery.service;

import com.project.delivery.repository.MemberRepository;
import com.project.delivery.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;


}
