package com.project.delivery.service;

import com.project.delivery.dto.TokenDto;
import com.project.delivery.dto.request.TokenRequestDto;
import com.project.delivery.dto.response.MemberResponseDto;
import com.project.delivery.dto.response.ResponseDto;
import com.project.delivery.entity.Customer;
import com.project.delivery.entity.RefreshToken;
import com.project.delivery.dto.request.MemberRequestDto;
import com.project.delivery.entity.Restaurant;
import com.project.delivery.repository.CustomerRepository;
import com.project.delivery.repository.RefreshTokenRepository;
import com.project.delivery.repository.RestaurantRepository;
import com.project.delivery.security.JwtFilter;
import com.project.delivery.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseDto<?> signup(MemberRequestDto memberRequestDto) {
        if (restaurantRepository.existsByUsername(memberRequestDto.getUsername()) ||
                customerRepository.existsByUsername(memberRequestDto.getUsername())) {
            // 식당/손님 유저네임 unique
            return ResponseDto.fail(400, "Bad Request", "중복된 아이디 입니다");
        }
        if (memberRequestDto.isRestaurant()) {
            // 식당 회원가입
            Restaurant restaurant = new Restaurant(memberRequestDto, passwordEncoder);
            restaurantRepository.save(restaurant);
            return ResponseDto.success(new MemberResponseDto(restaurant));
        } else {
            // 손님 회원가입
            Customer customer = new Customer(memberRequestDto, passwordEncoder);
            customerRepository.save(customer);
            return ResponseDto.success(new MemberResponseDto(customer));
        }
    }
    @Transactional
    public ResponseDto<?> login(MemberRequestDto memberRequestDto, HttpServletResponse response) {
        // TODO: return ResponseDto.fail for invalid login (currently exception is thrown)

        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        MemberResponseDto memberResponseDto;

        if (memberRequestDto.isRestaurant()) {
            // 식당 로그인 처리
            Restaurant restaurant = restaurantRepository.findByUsername(memberRequestDto.getUsername()).orElse(null);

            if (restaurant == null) {
                // 해당 username 없음
                return ResponseDto.fail(404, "Not Found", "사용자 정보를 찾을 수 없습니다.");
            }
            if(!passwordEncoder.matches(memberRequestDto.getPassword(), restaurant.getPassword())) {
                return ResponseDto.fail(400, "Bad Request", "패스워드가 일치하지 않습니다.");
            }
            memberResponseDto = new MemberResponseDto(restaurant);
        } else {
            // 손님 로그인 처리
            Customer customer = customerRepository.findByUsername(memberRequestDto.getUsername()).orElse(null);

            if (customer == null) {
                // 해당 username 없음
                return ResponseDto.fail(404, "Not Found", "사용자 정보를 찾을 수 없습니다.");
            }
            if(!passwordEncoder.matches(memberRequestDto.getPassword(), customer.getPassword())) {
                return ResponseDto.fail(400, "Bad Request", "패스워드가 일치하지 않습니다.");
            }
            memberResponseDto = new MemberResponseDto(customer);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken refreshToken = RefreshToken.builder()
                    .key(authentication.getName())
                    .value(tokenDto.getRefreshToken())
                    .build();

        refreshTokenRepository.save(refreshToken);

        response.setHeader(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        response.setHeader("Refresh-Token", tokenDto.getRefreshToken());

        return ResponseDto.success(memberResponseDto);
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        if(!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh 토큰이 유효하지 않습니다.");
        }
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃된 사용자입니다."));
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }

}