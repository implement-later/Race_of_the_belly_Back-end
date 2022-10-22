package com.project.delivery.service;

import com.project.delivery.entity.Authority;
import com.project.delivery.entity.Member;
import com.project.delivery.entity.Restaurant;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class MemberDetailsImpl implements UserDetails {

    private final Member member;

    private final Restaurant restaurant;

    public MemberDetailsImpl(Member member) {
        this.member = member;
        this.restaurant = null;
    }

    public MemberDetailsImpl(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.member = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Authority authority = member.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority.toString());

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
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
}
