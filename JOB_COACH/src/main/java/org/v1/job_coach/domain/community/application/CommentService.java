package org.v1.job_coach.domain.community.application;

import org.springframework.web.bind.annotation.PathVariable;
import org.v1.job_coach.domain.community.dto.request.CommentRequestDto;
import org.v1.job_coach.domain.community.dto.response.CommentResponseDto;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.user.domain.User;

import java.util.List;

public interface CommentService {

    ResultResponseDto<?> saveComment(Long boardId, CommentRequestDto dto, User user);

    ResultResponseDto<?> updateComment(Long commentId, CommentRequestDto dto, User user, Long board_id);

    ResultResponseDto<?> deleteComment(Long commentId, User user, Long board_id);

    ResultResponseDto<?> getComments(Long boardId, int page);
}