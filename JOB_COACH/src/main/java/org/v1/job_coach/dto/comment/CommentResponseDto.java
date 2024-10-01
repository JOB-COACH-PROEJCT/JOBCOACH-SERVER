package org.v1.job_coach.dto.comment;

import org.v1.job_coach.entity.community.Comment;

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
