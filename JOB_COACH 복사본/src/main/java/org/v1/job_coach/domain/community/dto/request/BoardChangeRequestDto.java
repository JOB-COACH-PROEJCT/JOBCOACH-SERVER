package org.v1.job_coach.domain.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record BoardChangeRequestDto(
        @Schema(description = "수정할 제목", defaultValue = "제목을 수정해보자 !")
        String title,
        @Schema(description = "수정할 내용", defaultValue = "안녕하세요. 게시글을 수정했습니다!")
        String content
        ) {
}
