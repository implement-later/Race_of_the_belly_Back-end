package com.project.delivery.service;

import com.project.delivery.entity.Customer;
import com.project.delivery.entity.Restaurant;
import com.project.delivery.repository.CustomerRepository;
import com.project.delivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username).orElse(null);
        Restaurant restaurant = restaurantRepository.findByUsername(username).orElse(null);

        if (customer == null && restaurant == null) {
            throw new UsernameNotFoundException(String.format("Username %s does not exists", username));
        } else if (customer == null) {
            return new MemberDetailsImpl(restaurant);
        } else {
            return new MemberDetailsImpl(customer);
        }
    }
}
