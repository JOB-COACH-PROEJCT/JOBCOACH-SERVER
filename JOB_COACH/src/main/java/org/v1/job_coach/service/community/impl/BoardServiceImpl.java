package org.v1.job_coach.service.community.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v1.job_coach.dto.board.BoardChangeDto;
import org.v1.job_coach.dto.board.BoardRequestDto;
import org.v1.job_coach.dto.board.BoardResponseDto;
import org.v1.job_coach.entity.community.Board;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.exception.CustomException;
import org.v1.job_coach.exception.Error;
import org.v1.job_coach.repository.community.BoardRepository;
import org.v1.job_coach.service.community.BoardService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final Logger logger = LoggerFactory.getLogger(BoardService.class);

    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public BoardResponseDto getContent(Long id) {
        logger.info("[getContent] : 게시글 반환");

        Board board = boardRepository.findById(id).get();

        return BoardResponseDto.toDto(board);
    }

    @Override
    @Transactional
    public BoardResponseDto saveBoard(BoardRequestDto boardRequestDto, User user) {
        logger.info("[saveBoard] 게시글 저장 : {}", boardRequestDto.toString());

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
    public BoardResponseDto changeBoard(Long id, String title, String content) {
        logger.info("[changeBoard] : 게시글 수정");

        Board findBoard = boardRepository.getById(id);
        findBoard.setTitle(title);
        findBoard.setContent(content);
        findBoard.setUpdateDate(LocalDateTime.now());
        Board board = boardRepository.save(findBoard);

        return BoardResponseDto.toDto(board);
    }

    @Override
    @Transactional
    public BoardResponseDto changeBoard(Long id, BoardChangeDto boardChangeDto, User user) {
        logger.info("[changeBoard] : 게시글 수정");

        Board findBoard = boardRepository.findById(id).orElseThrow(() -> new CustomException(Error.BOARD_NOT_FOUND));
        if (!findBoard.getUser().getPid().equals(user.getPid())){
            throw new CustomException(Error.ACCESS_DENIED);
        }
        return findBoard.updateBoard(boardChangeDto);
    }

    @Override
    public void deleteBoard(Long id) {
        logger.info("[deleteBoard] : 게시글 삭제");
        boardRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBoard(Long id, User user) {
        logger.info("[deleteBoard] : 게시글 삭제");
        Board board = boardRepository.findById(id).orElseThrow(() -> new CustomException(Error.BOARD_NOT_FOUND));

        if (!board.getUser().getPid().equals(user.getPid())){
            throw new CustomException(Error.NOT_AUTHOR);
        }
        boardRepository.deleteById(id);
    }
}
