package com.havit.finalbe.controller;

import com.havit.finalbe.dto.request.LoginRequestDto;
import com.havit.finalbe.dto.request.SignupRequestDto;
import com.havit.finalbe.dto.response.MemberProfileResponseDto;
import com.havit.finalbe.dto.response.MemberResponseDto;
import com.havit.finalbe.dto.response.MessageResponseDto;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import com.havit.finalbe.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = {"멤버 API"})
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "회원 정보를 입력하고 회원가입을 완료합니다")
    @PostMapping("/signup")
    public MemberResponseDto signup(@RequestBody SignupRequestDto signupRequestDto) {
        return memberService.signup(signupRequestDto);
    }

    @Operation(summary = "로그인", description = "회원 정보 시 기입한 이메일과 비밀번호로 로그인을 합니다.(Access Token, Refresh Token 생성)")
    @PostMapping("/login")
    public MemberResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return memberService.login(loginRequestDto, response);
    }

    @Operation(summary = "로그아웃", description = "로그아웃을 합니다.(Refresh Token 파기)")
    @RequestMapping(value = "/auth/logout", method = RequestMethod.POST)
    public MessageResponseDto logout(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memberService.logout(request, userDetails);
    }

    @Operation(summary = "토큰 재발급", description = "토큰 재발급합니다")
    @PostMapping("/reissue")
    public String reissue(HttpServletRequest request, HttpServletResponse response) {
        return memberService.reissue(request,response);
    }

    @Operation(summary = "로그인 멤버 정보", description = "로그인한 멤버 정보를 반환합니다.")
    @GetMapping("/auth/info")
    public MemberResponseDto getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memberService.getMyInfo(userDetails);
    }

    @Operation(summary = "다른 멤버 정보", description = "다른 멤버 정보를 조회합니다.")
    @GetMapping("/auth/info/{memberId}")
    public MemberProfileResponseDto getMemberInfo(@PathVariable Long memberId) {
        return memberService.getMemberInfo(memberId);
    }

}
