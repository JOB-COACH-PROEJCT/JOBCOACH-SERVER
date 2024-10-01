package org.v1.job_coach.domain.community.application;

import org.v1.job_coach.domain.community.dto.request.CommentRequestDto;
import org.v1.job_coach.domain.community.dto.response.CommentResponseDto;
import org.v1.job_coach.user.domain.User;

import java.util.List;

public interface CommentService {

    CommentResponseDto saveComment(Long boardId, CommentRequestDto dto, User user);

    CommentResponseDto updateComment(Long commentId, CommentRequestDto dto, User user);

    void deleteComment(Long commentId, User user);

    List<CommentResponseDto> getComments(Long boardId, int page);
}