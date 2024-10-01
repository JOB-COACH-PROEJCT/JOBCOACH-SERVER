package org.v1.job_coach.domain.community.dto.response;

import org.v1.job_coach.user.dto.response.UserResponseDto;
import org.v1.job_coach.domain.community.domain.Board;

import java.time.LocalDateTime;

public record BoardResponseDto(
        Long id,
        String title,
        String content,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        UserResponseDto userResponseDto
) {
    public static BoardResponseDto toDto(Board board) {
        return new BoardResponseDto(board.getId(), board.getTitle(), board.getContent(), board.getCreateDate(), board.getUpdateDate(), UserResponseDto.toDto(board.getUser()));
    }
}
