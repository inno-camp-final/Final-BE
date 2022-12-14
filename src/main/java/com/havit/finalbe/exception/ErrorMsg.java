package com.havit.finalbe.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorMsg {
    ENTITY_NOT_FOUND("ENTITY_NOT_FOUND","데이터가 존재하지 않습니다."),
    INVALID_ERROR("INVALID_ERROR","에러 발생"),

    //회원가입, 로그인 관련 에러
    EMPTY_VALUE("EMPTY_VALUE", "정보를 입력하세요."),
    INVALID_EMAIL("INVALID_EMAIL", "필드는 100자 이하여야 하며, @ 기호 전까지 64자 이하여야 합니다."),
    INVALID_PASSWORD("INVALID_PASSWORD", "비밀번호 최소 8자 이상 , 소문자 , 숫자 (0-9) 또는 특수문자 (!@#$%^&*)"),
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", "중복된 이메일 주소가 있습니다."),

    // 회원 관련 에러
    MEMBER_NOT_FOUND("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCHED("PASSWORD_NOT_MATCHED", "비밀번호가 일치하지 않습니다."),

    // 권한 요청 시 Access 토큰을 보내지 않은 경우
    INVALID_LOGIN("INVALID_LOGIN", "로그인이 필요합니다."),

    // 로그아웃시 refresh 토큰을 보내지 않은 경우
    NEED_REFRESH_TOKEN("NEED_REFRESH_TOKEN","Refresh Token이 필요합니다."),

    // 유효하지 않은 토큰
    INVALID_TOKEN("INVALID_TOKEN", "유효하지 않은 Token 입니다."),

    // 일치하지 않은 토큰
    REFRESH_TOKEN_NOT_MATCHED("REFRESH_TOKEN_NOT_MATCHED", "토큰이 일치하지 않습니다."),

    // 만료된 토큰
    EXPIRED_ACCESS_TOKEN("EXPIRED_ACCESS_TOKEN", "만료된 Access Token 입니다."),
    EXPIRED_REFRESH_TOKEN("EXPIRED_REFRESH_TOKEN", "만료된 Refresh Token 입니다."),

    // 그룹, 댓글
    GROUP_NOT_FOUND("GROUP_NOT_FOUND", "해당 그룹을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND("COMMENT_NOT_FOUND", "해당 댓글을 찾을 수 없습니다."),
    CERTIFY_NOT_FOUND("CERTIFY_NOT_FOUND", "해당 인증샷을 찾을 수 없습니다."),
    MEMBER_NOT_MATCHED("MEMBER_NOT_MATCHED", "작성자가 아닙니다."),

    // 태그
    TAG_NOT_FOUND("TAG_NOT_FOUND", "태그를 찾을 수 없습니다."),

    // 참여하기
    DUPLICATE_PARTICIPATION("DUPLICATE_PARTICIPATION", "이미 참여했습니다."),
    PARTICIPATION_NOT_FOUND("PARTICIPATION_NOT_FOUND", "참여 내역이 없습니다."),

    // 이미지
    IMAGE_NOT_FOUND("IMAGE_NOT_FOUND", "삭제할 이미지가 없습니다.");

    private final String code;
    private final String message;
}
