package com.havit.finalbe.controller;

import com.havit.finalbe.dto.response.AllGroupListResponseDto;
import com.havit.finalbe.dto.response.UnchallengerResponseDto;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import com.havit.finalbe.service.MainService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"메인 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/main")
public class MainController {

    private final MainService mainService;

    @Operation(summary = "그룹 검색", description = "그룹 제목, 그룹 작성자 닉네임으로 전체 그룹을 검색합니다.")
    @GetMapping("/search")
    public List<AllGroupListResponseDto> searchGroup(@RequestParam(value = "search") String searchWord,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mainService.searchGroup(searchWord, userDetails);
    }

    @Operation(summary = "알림 보낼 그룹원 목록 조회", description = "매일 챌린지에 참여하지 않은 멤버 목록을 불러옵니다.")
    @GetMapping("/")
    public List<UnchallengerResponseDto> searchGroup(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mainService.getUnchallengerList(userDetails);
    }
}
