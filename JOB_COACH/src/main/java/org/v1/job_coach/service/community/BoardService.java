package org.v1.job_coach.service.community;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.v1.job_coach.dto.board.BoardChangeDto;
import org.v1.job_coach.dto.board.BoardRequestDto;
import org.v1.job_coach.dto.board.BoardResponseDto;
import org.v1.job_coach.entity.User;

public interface BoardService {
    BoardResponseDto getContent(Long id, User user);
    BoardResponseDto saveBoard(BoardRequestDto boardRequestDto, User user);
    BoardResponseDto changeBoard(Long boardId, BoardChangeDto boardChangeDto, User user);
    void deleteBoard(Long id, User user);
    Page<BoardResponseDto> getBoards(int page);
    Page<BoardResponseDto> searchBoard(Pageable pageable, String title);
}
