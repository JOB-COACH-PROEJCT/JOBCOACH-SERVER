package org.v1.job_coach.dto.board;

import org.v1.job_coach.entity.Board;
import org.v1.job_coach.entity.User;

import java.time.LocalDateTime;

public record BoardResponseDto(
        Long id,
        String title,
        String content,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        User user
) {
    public static BoardResponseDto toDto(Board board) {
        return new BoardResponseDto(board.getId(), board.getTitle(), board.getContent(), board.getCreateDate(), board.getUpdateDate(), board.getUser());
    }
}
