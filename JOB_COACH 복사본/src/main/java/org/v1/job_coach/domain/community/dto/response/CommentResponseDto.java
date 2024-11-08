package org.v1.job_coach.domain.community.dto.response;

import org.v1.job_coach.domain.community.domain.Comment;
import org.v1.job_coach.global.util.DateFormatter;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long id,
        String content,
        String userName,
        String createdAt
) {
    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getName(),
                DateFormatter.getDateNow(comment.getCreatedAt()));
    }
}
