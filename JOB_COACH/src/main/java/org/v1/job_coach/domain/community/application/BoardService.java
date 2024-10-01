package org.v1.job_coach.domain.community.application;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.v1.job_coach.domain.community.dto.request.BoardChangeRequestDto;
import org.v1.job_coach.domain.community.dto.request.BoardRequestDto;
import org.v1.job_coach.domain.community.dto.response.BoardResponseDto;
import org.v1.job_coach.user.domain.User;

public interface BoardService {
    BoardResponseDto getContent(Long id, User user);
    BoardResponseDto saveBoard(BoardRequestDto boardRequestDto, User user);
    BoardResponseDto changeBoard(Long boardId, BoardChangeRequestDto boardChangeRequestDto, User user);
    void deleteBoard(Long id, User user);
    Page<BoardResponseDto> getBoards(int page);
    Page<BoardResponseDto> searchBoard(Pageable pageable, String title);
}
