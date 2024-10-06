package org.v1.job_coach.domain.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommentRequestDto(
        @Schema(description = "댓글 내용", defaultValue = "좋을 글 잘 보고 갑니다~")
        String content
) {
}
