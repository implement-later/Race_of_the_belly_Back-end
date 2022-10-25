package com.project.delivery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class MemberRequestDto {
    private String name;
    @NotBlank
    @Pattern(regexp="^[a-zA-Z0-9]{4,12}$", message = "닉네임은 최소 4자 이상, 12자 이하 알파벳 대소문자(a-z, A-Z), 숫자(0-9)로 구성됩니다.")
    private String username;

    @NotBlank
    @Pattern(regexp= "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "비밀번호는 최소 8자 이상, 20자 이하 알파벳 대소문자, 숫자(0-9), 특수문자로 구성됩니다.")
    private String password;

    private boolean isRestaurant;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
