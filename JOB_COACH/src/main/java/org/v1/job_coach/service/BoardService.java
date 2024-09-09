package org.v1.job_coach.service;


import org.v1.job_coach.dto.board.BoardChangeDto;
import org.v1.job_coach.dto.board.BoardRequestDto;
import org.v1.job_coach.dto.board.BoardResponseDto;
import org.v1.job_coach.entity.User;

public interface BoardService {
    BoardResponseDto getContent(Long id);

    BoardResponseDto saveBoard(BoardRequestDto boardRequestDto, User user);
    BoardResponseDto changeBoard(Long id, String title, String content);
    BoardResponseDto changeBoard(BoardChangeDto boardChangeDto, User user);
    void deleteBoard(Long id);

    void deleteBoard(Long id, User user);
}
