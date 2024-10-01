package org.v1.job_coach.domain.community.dto.response;

import org.v1.job_coach.domain.community.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long id,
        String content,
        String userName,
        LocalDateTime createdAt
) {
    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getName(),
                comment.getCreatedAt());
    }
}
