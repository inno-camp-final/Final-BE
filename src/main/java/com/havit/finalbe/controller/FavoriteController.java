package com.havit.finalbe.controller;

import com.havit.finalbe.dto.request.FavoriteRequestDto;
import com.havit.finalbe.dto.response.FavoriteResponseDto;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import com.havit.finalbe.service.FavoriteService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"즐겨찾기 API"})
@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "그룹 즐겨찾기", description = "해당하는 그룹을 즐겨찾기/즐겨찾기 취소 합니다.")
    @PostMapping("/api/auth/favorite")
    public FavoriteResponseDto favorites(@RequestBody FavoriteRequestDto favoriteRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return favoriteService.favorites(favoriteRequestDto, userDetails);
    }
}
