package com.havit.finalbe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "대댓글 응답Dto")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCommentResponseDto {

    @Schema(description = "대댓글 id", example = "1")
    private Long subCommentId;

    @Schema(description = "댓글 id", example = "1")
    private Long commentId;

    @Schema(description = "닉네임", example = "김병처리")
    private String nickname;

    @Schema(description = "프로필 이미지 Url", example = "aws 이미지 Url")
    private String profileUrl;

    @Schema(description = "대댓글 내용", example = "댓글 달아주셔서 감사합니다.")
    private String content;

    @Schema(description = "작성 일시", example = "2022-07-25T12:43:01.226062")
    private String dateTime;
}
