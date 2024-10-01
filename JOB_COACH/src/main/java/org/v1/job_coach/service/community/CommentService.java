package org.v1.job_coach.service.community;

import org.v1.job_coach.dto.comment.CommentRequestDto;
import org.v1.job_coach.dto.comment.CommentResponseDto;
import org.v1.job_coach.entity.User;

import java.util.List;

public interface CommentService {

    CommentResponseDto saveComment(Long boardId, CommentRequestDto dto, User user);

    CommentResponseDto updateComment(Long commentId, CommentRequestDto dto, User user);

    void deleteComment(Long commentId, User user);

    List<CommentResponseDto> getComments(Long boardId, int page);
}
