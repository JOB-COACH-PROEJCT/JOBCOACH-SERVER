package org.v1.job_coach.domain.community.application;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.v1.job_coach.domain.community.dto.request.BoardChangeRequestDto;
import org.v1.job_coach.domain.community.dto.request.BoardRequestDto;
import org.v1.job_coach.domain.community.dto.response.BoardResponseDto;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.user.domain.User;

public interface BoardService {
    ResultResponseDto<?> getContent(Long id, User user);
    ResultResponseDto<?> saveBoard(BoardRequestDto boardRequestDto, User user);
    ResultResponseDto<?> changeBoard(Long boardId, BoardChangeRequestDto boardChangeRequestDto, User user);
    ResultResponseDto<?> deleteBoard(Long id, User user);
    ResultResponseDto<?> getBoards(int page);
    ResultResponseDto<?> searchBoard(Pageable pageable, String title);
}
