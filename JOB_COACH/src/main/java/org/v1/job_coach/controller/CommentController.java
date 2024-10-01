/*
package org.v1.job_coach.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.v1.job_coach.dto.comment.CommentRequestDto;
import org.v1.job_coach.dto.comment.CommentResponseDto;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.service.community.CommentService;

import java.util.List;

@Tag(name = "Comments", description = "댓글 API")
@RestController
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{board_id}/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long board_id,
                                                                @RequestParam(defaultValue = "0") int page) {
        List<CommentResponseDto> comments = commentService.getComments(board_id, page);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    @Parameters({@Parameter(name = "Authorization", description = "로그인 성공 후 발급 받은 access_token", required = true)})
    @PostMapping("/{board_id}/comments")
    public ResponseEntity<CommentResponseDto> saveComment(@AuthenticationPrincipal User user,
                                                          @PathVariable Long board_id,
                                                          @RequestBody CommentRequestDto dto) {
        CommentResponseDto commentResponseDto = commentService.saveComment(board_id, dto, user);
        return ResponseEntity.status(HttpStatus.OK).body(commentResponseDto);
    }

    @Parameters({@Parameter(name = "Authorization", description = "로그인 성공 후 발급 받은 access_token", required = true)})
    @PutMapping("/{board_id}/comments/{comment_id}")
    public ResponseEntity<CommentResponseDto> updateComment(@AuthenticationPrincipal User user,
                                                            @PathVariable Long comment_id,
                                                            @RequestBody CommentRequestDto dto) {
        CommentResponseDto commentResponseDto = commentService.updateComment(comment_id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(commentResponseDto);
    }

    @Parameters({@Parameter(name = "Authorization", description = "로그인 성공 후 발급 받은 access_token", required = true)})
    @DeleteMapping("/{board_id}/comments/{comment_id}")
    public ResponseEntity<String> deleteComment(@AuthenticationPrincipal User user,
                                                @PathVariable Long comment_id) {
        commentService.deleteComment(comment_id);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 성공적으로 삭제되었습니다.");
    }
}
*/
