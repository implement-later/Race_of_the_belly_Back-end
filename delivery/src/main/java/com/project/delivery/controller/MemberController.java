package com.project.delivery.controller;

import com.project.delivery.dto.TokenDto;
import com.project.delivery.dto.request.MemberRequestDto;
import com.project.delivery.dto.request.TokenRequestDto;
import com.project.delivery.dto.response.ResponseDto;
import com.project.delivery.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/signup")
    public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        return memberService.signup(memberRequestDto);
    }

    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody MemberRequestDto memberRequestDto, HttpServletResponse response) {

        return memberService.login(memberRequestDto, response);
    }

    @PostMapping("/reissue")
    public ResponseDto<?> reissue(@RequestBody TokenRequestDto tokenRequestDto) {

        TokenDto tokenDto = memberService.reissue(tokenRequestDto);
        return ResponseDto.success(tokenDto);
    }
}