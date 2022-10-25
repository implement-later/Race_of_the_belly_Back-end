package com.project.delivery.service;

import com.project.delivery.entity.Authority;
import com.project.delivery.entity.Customer;
import com.project.delivery.entity.Restaurant;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class MemberDetailsImpl implements UserDetails {

    private final Customer customer;

    private final Restaurant restaurant;

    private final boolean isCustomer;

    public MemberDetailsImpl(Customer customer) {
        this.customer = customer;
        this.restaurant = null;
        this.isCustomer = true;
    }

    public MemberDetailsImpl(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.customer = null;
        this.isCustomer = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Authority authority = this.isCustomer ? Authority.ROLE_CUSTOMER : Authority.ROLE_RESTAURANT;

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority.toString());

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getPassword() {
        if (restaurant == null)
            return customer.getPassword();
        else
            return restaurant.getPassword();
    }

    @Override
    public String getUsername() {
        return restaurant == null ? customer.getUsername() : restaurant.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isCustomer() {
        return this.isCustomer;
    }

}
