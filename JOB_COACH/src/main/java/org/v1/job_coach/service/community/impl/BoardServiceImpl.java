package org.v1.job_coach.service.community.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v1.job_coach.dto.board.BoardChangeDto;
import org.v1.job_coach.dto.board.BoardRequestDto;
import org.v1.job_coach.dto.board.BoardResponseDto;
import org.v1.job_coach.dto.review.ResponseReviewDto;
import org.v1.job_coach.entity.community.Board;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.entity.review.Review;
import org.v1.job_coach.exception.CustomException;
import org.v1.job_coach.exception.Error;
import org.v1.job_coach.repository.UserRepository;
import org.v1.job_coach.repository.community.BoardRepository;
import org.v1.job_coach.service.community.BoardService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final Logger logger = LoggerFactory.getLogger(BoardService.class);

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public BoardResponseDto getContent(Long id, User user) {
        logger.info("[getContent] : 게시글 반환");
        Board board = boardRepository.findById(id).orElseThrow(() -> new CustomException(Error.NOT_FOUND_BOARD));
        return BoardResponseDto.toDto(board);
    }

    @Override
    @Transactional
    public BoardResponseDto saveBoard(BoardRequestDto boardRequestDto, User user) {
        logger.info("[saveBoard] 게시글 저장 : {}", boardRequestDto.toString());
        isAccess(user);

        Board board = new Board();
        board.setTitle(boardRequestDto.title());
        board.setContent(boardRequestDto.content());
        board.setCreateDate(LocalDateTime.now());
        board.setUpdateDate(LocalDateTime.now());
        board.setUser(user);

        Board savedBoard = boardRepository.save(board);

        return BoardResponseDto.toDto(savedBoard);
    }

    @Override
    @Transactional
    public BoardResponseDto changeBoard(Long id, BoardChangeDto boardChangeDto, User user) {
        logger.info("[changeBoard] : 게시글 수정");
        isAccess(user);

        Board findBoard = boardRepository.findById(id).orElseThrow(() -> new CustomException(Error.NOT_FOUND_BOARD));
        return findBoard.updateBoard(boardChangeDto);
    }

    @Override
    @Transactional
    public void deleteBoard(Long id, User user) {
        logger.info("[deleteBoard] : 게시글 삭제");
        isAccess(user);
        boardRepository.findById(id).orElseThrow(() -> new CustomException(Error.NOT_FOUND_BOARD));

        boardRepository.deleteById(id);
    }

    @Transactional
    public Page<BoardResponseDto> getBoards(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Board> boards = boardRepository.findAll(pageable);

        return boards.map(BoardResponseDto::toDto);
    }

    public Page<BoardResponseDto> searchBoard(Pageable pageable, String title) {
        List<Board> allBoards = boardRepository.findAll();

        List<Board> filteredBoard = allBoards.stream()
                .filter(board -> board.getTitle().contains(title))
                .toList();

        int start = Math.min((int) pageable.getOffset(), filteredBoard.size());
        int end = Math.min(start + pageable.getPageSize(), filteredBoard.size());
        List<BoardResponseDto> reviewDtos = filteredBoard.subList(start, end)
                .stream()
                .map(BoardResponseDto::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(reviewDtos, pageable, filteredBoard.size());
    }

    public void isAccess(User user) {
        userRepository.findById(user.getPid()).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
    }

    public void isAccess(User user, Long boardID) {
        if (!user.getPid().equals(boardID)) {
            throw new CustomException(Error.ACCESS_DENIED);
        }
    }
}
