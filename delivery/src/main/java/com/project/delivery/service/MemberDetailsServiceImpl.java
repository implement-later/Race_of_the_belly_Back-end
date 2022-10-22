package com.project.delivery.service;

import com.project.delivery.entity.Member;
import com.project.delivery.entity.Restaurant;
import com.project.delivery.repository.MemberRepository;
import com.project.delivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).orElse(null);
        Restaurant restaurant = restaurantRepository.findByUsername(username).orElse(null);

        if (member == null && restaurant == null) {
            throw new UsernameNotFoundException(String.format("Username %s does not exists", username));
        } else if (member == null) {
            return new MemberDetailsImpl(restaurant);
        } else {
            return new MemberDetailsImpl(member);
        }
    }
}
