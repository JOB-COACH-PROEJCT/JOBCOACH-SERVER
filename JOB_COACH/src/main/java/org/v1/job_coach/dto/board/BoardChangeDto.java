package org.v1.job_coach.dto.board;

public record BoardChangeDto(
        Long id,
        String title,
        String content
) {
}
