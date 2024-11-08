package org.v1.job_coach.domain.community.application.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v1.job_coach.domain.community.application.BoardService;
import org.v1.job_coach.domain.community.dao.BoardRepository;
import org.v1.job_coach.domain.community.domain.Board;
import org.v1.job_coach.domain.community.dto.request.BoardChangeRequestDto;
import org.v1.job_coach.domain.community.dto.request.BoardRequestDto;
import org.v1.job_coach.domain.community.dto.response.BoardResponseDto;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.user.dao.UserRepository;
import org.v1.job_coach.user.domain.User;

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
    public ResultResponseDto<?> getContent(Long id, User user) {
        logger.info("[getContent] : 게시글 반환");

        Board board = isBoardPresent(id);

        BoardResponseDto boardDto = BoardResponseDto.toDto(board);

        return ResultResponseDto.toDataResponseDto(
                200,
                "게시글이 성공적으로 반환되었습니다.",
                boardDto);

    }

    @Override
    @Transactional
    public ResultResponseDto<?> saveBoard(BoardRequestDto boardRequestDto, User user) {
        logger.info("[saveBoard] 게시글 저장 : {}", boardRequestDto.toString());
        isAccess(user);

        boardRepository.save(new Board(boardRequestDto, user));

        return ResultResponseDto.toResultResponseDto(
                201,
                "게시글이 성공적으로 저장되었습니다.");
    }

    @Override
    @Transactional
    public ResultResponseDto<?> changeBoard(Long id, BoardChangeRequestDto boardChangeRequestDto, User user) {
        logger.info("[changeBoard] : 게시글 수정");
        isAccess(user);
        extracted(user, id);
        Board findBoard = isBoardPresent(id);
        findBoard.updateBoard(boardChangeRequestDto);

        return ResultResponseDto.toResultResponseDto(
                200,
                "게시글이 성공적으로 수정되었습니다.");
    }


    @Override
    @Transactional
    public ResultResponseDto<?> deleteBoard(Long id, User user) {
        logger.info("[deleteBoard] : 게시글 삭제");
        isAccess(user);
        extracted(user, id);
        isBoardPresent(id);

        boardRepository.deleteById(id);

        return ResultResponseDto.toResultResponseDto(
                200,
                "게시글이 성공적으로 삭제되었습니다.");
    }

    @Transactional
    public ResultResponseDto<?> getBoards(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Board> boards = boardRepository.findAll(pageable);

        if (page >= boards.getTotalPages()){
            throw new CustomException(Error.INVALID_PAGE);
        }

        return ResultResponseDto.toDataResponseDto(
                200,
                "게시글이 성공적으로 반환되었습니다.",
                boards.map(BoardResponseDto::toDto));
    }

    public ResultResponseDto<?> searchBoard(Pageable pageable, String title) {
        List<Board> allBoards = boardRepository.findAll();

        List<Board> filteredBoard = allBoards.stream()
                .filter(board -> board.getTitle().contains(title))
                .toList();

        int start = Math.min((int) pageable.getOffset(), filteredBoard.size());
        int end = Math.min(start + pageable.getPageSize(), filteredBoard.size());
        List<BoardResponseDto> boardDtos = filteredBoard.subList(start, end)
                .stream()
                .map(BoardResponseDto::toDto)
                .toList();

        if (start >= filteredBoard.size()) {
            throw new CustomException(Error.INVALID_PAGE);
        }

        if (boardDtos.isEmpty()) {
            return ResultResponseDto.toResultResponseDto(
                    204,
                    "해당 검색 조건에 일치하는 결과가 없습니다.");
        }


        return ResultResponseDto.toDataResponseDto(
                200,
                "게시글이 성공적으로 반환되었습니다.",
                new PageImpl<>(boardDtos, pageable, filteredBoard.size()));
    }


    public void isAccess(User user) {
        userRepository.findById(user.getPid()).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
    }

    public void extracted(User user, Long boardID) {
        Long boardCreatedUserPid = isBoardPresent(boardID).getUser().getPid();
        if (!user.getPid().equals(boardCreatedUserPid)) {
            throw new CustomException(Error.ACCESS_DENIED);
        }
    }
    private Board isBoardPresent(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new CustomException(Error.NOT_FOUND_BOARD));
    }

}
