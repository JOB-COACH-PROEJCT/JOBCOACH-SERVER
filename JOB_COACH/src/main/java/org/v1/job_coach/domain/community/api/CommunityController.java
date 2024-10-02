package org.v1.job_coach.domain.community.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.v1.job_coach.domain.community.application.BoardService;
import org.v1.job_coach.domain.community.application.CommentService;
import org.v1.job_coach.domain.community.dto.request.BoardChangeRequestDto;
import org.v1.job_coach.domain.community.dto.request.BoardRequestDto;
import org.v1.job_coach.domain.community.dto.request.CommentRequestDto;
import org.v1.job_coach.domain.community.dto.response.BoardResponseDto;
import org.v1.job_coach.domain.community.dto.response.CommentResponseDto;
import org.v1.job_coach.user.domain.User;

import java.util.List;

@Tag(name = "community", description = "커뮤니티 API")
@RestController
@RequestMapping("/api/v1/community")
public class CommunityController {

    private final CommentService commentService;
    private final BoardService boardService;

    @Autowired
    public CommunityController(CommentService commentService, BoardService boardService) {
        this.commentService = commentService;
        this.boardService = boardService;
    }

    @GetMapping("{board_id}")
    public ResponseEntity<?> getBoard(@AuthenticationPrincipal User user, @PathVariable Long board_id) {
        BoardResponseDto boardResponseDto = boardService.getContent(board_id, user);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponseDto);
    }

    @GetMapping
    @Operation(summary = "커뮤니티 게시글 조회 API", description = "모든 게시글을 반환하는 API이며, 페이징을 포함합니다."
            + "query string으로 page 번호를 주세요")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> getBoards(@RequestParam(defaultValue = "0") int page) {
        Page<BoardResponseDto> boards = boardService.getBoards(page);
        return ResponseEntity.status(HttpStatus.OK).body(boards);
    }

    @GetMapping("/search")
    @Operation(summary = "커뮤니티 게시글 검색 API", description = "제목에 검색한 키워드가 포함된 게시글을 반환하는 API이며, 페이징을 포함합니다."
            + "query string으로 page 번호를 주세요(size는 선택, 기본 10)")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true),
            @Parameter(name = "page", description = "페이지 번호, 0이상이어야 함, query string"),
            @Parameter(name = "size", description = "(선택적) 페이지당 컨텐츠 개수, 기본 10, query string")})
    public ResponseEntity<?> findByTitle(@RequestParam String title,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<BoardResponseDto> boardResponseDtos = boardService.searchBoard(pageable, title);

        if (boardResponseDtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("검색 된 후기가 없습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(boardResponseDtos);
    }

    @PostMapping
    @Operation(summary = "커뮤니티 게시글 저장 API", description = "게시글을 저장하는 API입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> saveBoard(@AuthenticationPrincipal User user,
                                       @RequestBody BoardRequestDto boardRequestDto) {
        BoardResponseDto boardResponseDto = boardService.saveBoard(boardRequestDto, user);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponseDto);
    }

    @PutMapping("{board_id}")
    @Operation(summary = "커뮤니티 게시글 수정 API", description = "게시글을 수정하는 API입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> changeBoard(@AuthenticationPrincipal User user,
                                         @PathVariable Long board_id,
                                         @RequestBody BoardChangeRequestDto boardChangeRequestDto) {
        BoardResponseDto boardResponseDto = boardService.changeBoard(board_id, boardChangeRequestDto, user);

        return ResponseEntity.status(HttpStatus.OK).body(boardResponseDto);
    }

    @DeleteMapping("{board_id}")
    @Operation(summary = "커뮤니티 게시글 삭제 API", description = "게시글을 삭제하는 API입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> deleteBoard(@AuthenticationPrincipal User user,
                                         @PathVariable Long board_id) {
        boardService.deleteBoard(board_id, user);
        return ResponseEntity.status(HttpStatus.OK).body("게시글을 성공적으로 삭제하였습니다.");
    }

    @GetMapping("{board_id}/comments")
    @Operation(summary = "커뮤니티 댓글 반환 API", description = "게시글의 댓글을 반환하는 API이며 페이징을 포함합니다." +
            "query string으로 page 번호를 주세요")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> getComments(@PathVariable Long board_id,
                                         @RequestParam(defaultValue = "0") int page) {
        List<CommentResponseDto> comments = commentService.getComments(board_id, page);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    @PostMapping("{board_id}/comments")
    @Operation(summary = "커뮤니티 댓글 저장 API", description = "댓글을 저장하는 API입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> saveComment(@AuthenticationPrincipal User user,
                                         @PathVariable Long board_id,
                                         @RequestBody CommentRequestDto dto) {
        CommentResponseDto commentResponseDto = commentService.saveComment(board_id, dto, user);
        return ResponseEntity.status(HttpStatus.OK).body(commentResponseDto);
    }

    @PutMapping("{board_id}/comments/{comment_id}")
    @Operation(summary = "커뮤니티 댓글 수정 API", description = "댓글을 수정하는 API입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> updateComment(@AuthenticationPrincipal User user,
                                           @PathVariable Long comment_id,
                                           @PathVariable Long board_id,
                                           @RequestBody CommentRequestDto dto) {
        CommentResponseDto commentResponseDto = commentService.updateComment(comment_id, dto, user, board_id);
        return ResponseEntity.status(HttpStatus.OK).body(commentResponseDto);
    }

    @DeleteMapping("{board_id}/comments/{comment_id}")
    @Operation(summary = "커뮤니티 댓글 삭제 API", description = "댓글을 삭제하는 API입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal User user,
                                           @PathVariable Long comment_id,
                                           @PathVariable Long board_id) {
        commentService.deleteComment(comment_id, user, board_id);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 성공적으로 삭제되었습니다.");
    }
}
