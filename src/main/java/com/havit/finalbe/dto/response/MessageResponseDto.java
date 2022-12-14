package com.havit.finalbe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "메세지 응답Dto")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {

    private String errorcode;

    @Schema(description = "메세지", example = "success")
    private String message;
}