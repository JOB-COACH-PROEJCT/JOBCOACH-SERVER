package org.v1.job_coach.domain.community.application.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v1.job_coach.domain.community.application.CommentService;
import org.v1.job_coach.domain.community.dao.BoardRepository;
import org.v1.job_coach.domain.community.dao.CommentRepository;
import org.v1.job_coach.domain.community.domain.Board;
import org.v1.job_coach.domain.community.domain.Comment;
import org.v1.job_coach.domain.community.dto.request.CommentRequestDto;
import org.v1.job_coach.domain.community.dto.response.CommentResponseDto;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.user.dao.UserRepository;
import org.v1.job_coach.user.domain.User;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BoardRepository boardRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public CommentResponseDto saveComment(Long boardId, CommentRequestDto dto, User user) {
        isLogin(user);

        Board board = isBoardPresent(boardId);

        Comment comment = new Comment(dto, user, board);
        commentRepository.save(comment);

        return CommentResponseDto.toDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto dto, User user, Long boardId) {
        isLogin(user);
        isAccess(user, commentId);
        Comment comment= isCommentPresent(commentId);

        comment.updateContent(dto.content());
        commentRepository.save(comment);

        return CommentResponseDto.toDto(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, User user, Long boardId) {
        isLogin(user);
        isAccess(user, commentId);
        isBoardPresent(boardId);
        Comment comment = isCommentPresent(commentId); // 댓글을 영속성 컨텍스트에서 로드
        commentRepository.delete(comment); // 댓글 삭제
        //commentRepository.delete(isCommentPresent(commentId));
    }

    @Transactional
    public List<CommentResponseDto> getComments(Long boardId, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Comment> comments = commentRepository.findByBoard_Id(boardId, pageable);

        return comments.stream()
                .map(CommentResponseDto::toDto)
                .collect(Collectors.toList());
    }

    private void isLogin(User user) {
        userRepository.findById(user.getPid()).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
    }

    private void isAccess(User user, Long commentId) {
        Comment comment = isCommentPresent(commentId);

        if (!user.getPid().equals(comment.getUser().getPid())) {
            throw new CustomException(Error.ACCESS_DENIED);
        }
    }

    private Comment isCommentPresent(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_COMMENT));
    }

    private Board isBoardPresent(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_BOARD));
    }

}