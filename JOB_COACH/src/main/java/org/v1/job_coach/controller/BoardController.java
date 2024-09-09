package org.v1.job_coach.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.v1.job_coach.dto.board.BoardChangeDto;
import org.v1.job_coach.dto.board.BoardRequestDto;
import org.v1.job_coach.dto.board.BoardResponseDto;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.service.BoardService;

@RestController
@RequestMapping("/api/v1")
public class BoardController {

        private final Logger logger = LoggerFactory.getLogger(BoardController.class);

        private final BoardService boardService;
        @Autowired
        public BoardController(BoardService boardService){
            this.boardService = boardService;
        }

        @GetMapping("/board")
        public ResponseEntity<BoardResponseDto> getBoard(Long id){
            BoardResponseDto boardResponseDto =boardService.getContent(id);
            return ResponseEntity.status(HttpStatus.OK).body(boardResponseDto);
        }
        @Parameters({@Parameter(name = "Authorization", description = "로그인 성공 후 발급 받은 access_token", required = true)})
        @PostMapping("/board")
        public ResponseEntity<BoardResponseDto> saveBoard(@AuthenticationPrincipal User user,
                                                          @RequestBody BoardRequestDto boardRequestDto){
            BoardResponseDto boardResponseDto = boardService.saveBoard(boardRequestDto, user);
            return ResponseEntity.status(HttpStatus.OK).body(boardResponseDto);
        }
        @Parameters({@Parameter(name = "Authorization", description = "로그인 성공 후 발급 받은 access_token", required = true)})
        @PutMapping("/board")
        public ResponseEntity<BoardResponseDto> changeBoard(@AuthenticationPrincipal User user,
                                                            @RequestBody BoardChangeDto boardChangeDto){

            BoardResponseDto boardResponseDto = boardService.changeBoard(boardChangeDto, user);

            return ResponseEntity.status(HttpStatus.OK).body(boardResponseDto);
        }
        @Parameters({@Parameter(name = "Authorization", description = "로그인 성공 후 발급 받은 access_token", required = true)})
        @DeleteMapping("/board")
        public ResponseEntity<String> deleteBoard(@AuthenticationPrincipal User user,
                                                  @RequestParam Long id){
            boardService.deleteBoard(id, user);
            return ResponseEntity.status(HttpStatus.OK).body("게시글을 성공적으로 삭제하였습니다.");
        }


    }