package org.v1.job_coach.domain.community.application;

import org.springframework.web.bind.annotation.PathVariable;
import org.v1.job_coach.domain.community.dto.request.CommentRequestDto;
import org.v1.job_coach.domain.community.dto.response.CommentResponseDto;
import org.v1.job_coach.user.domain.User;

import java.util.List;

public interface CommentService {

    CommentResponseDto saveComment(Long boardId, CommentRequestDto dto, User user);

    CommentResponseDto updateComment(Long commentId, CommentRequestDto dto, User user, Long board_id);

    void deleteComment(Long commentId, User user, Long board_id);

    List<CommentResponseDto> getComments(Long boardId, int page);
}