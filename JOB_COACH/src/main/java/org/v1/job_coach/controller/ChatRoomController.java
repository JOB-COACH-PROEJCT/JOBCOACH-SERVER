package org.v1.job_coach.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.v1.job_coach.dto.chat.AnswerRequestDto;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.entity.chat.Answer;
import org.v1.job_coach.entity.chat.ChatRoom;
import org.v1.job_coach.entity.chat.Consulting;
import org.v1.job_coach.entity.chat.Question;
import org.v1.job_coach.service.ConsultingService;
import org.v1.job_coach.service.chat.InterViewService;


@Slf4j
@RestController
@RequestMapping("/api/v1/interview")
public class ChatRoomController {

    private final InterViewService interViewService;
    private final ConsultingService consultingService;

    public ChatRoomController(InterViewService interViewService, ConsultingService consultingService) {
        this.interViewService = interViewService;

        this.consultingService = consultingService;
    }

    @PostMapping("/chat-rooms")
    @Parameters({@Parameter(name = "Authorization", description = "로그인 성공 후 발급 받은 access_token", required = true)})
    public ResponseEntity<?> createChatRoom(@AuthenticationPrincipal User user,
                                            @RequestParam String roomName) {
        ChatRoom chatRoom = interViewService.createChatRoom(user, roomName);
        log.info("[ChatRoom 생성] user: {}, roomName: {}", user, roomName);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoom);
    }

    @PutMapping("/chat-rooms/{chatRoomId}/deactivate")
    @Parameters({@Parameter(name = "Authorization", description = "로그인 성공 후 발급 받은 access_token", required = true)})
    public ResponseEntity<?> endChatRoom(@AuthenticationPrincipal User user,
                                         @PathVariable Long chatRoomId) {
        interViewService.deactivateChatRoom(user, chatRoomId);
        log.info("[ChatRoom 종료] chatRoomId: {}", chatRoomId);
        return ResponseEntity.status(HttpStatus.OK).body("모의면접을 종료합니다.");
    }

    @PostMapping("/chat-rooms/{chatRoomId}/answers")
    @Parameters({@Parameter(name = "Authorization", description = "로그인 성공 후 발급 받은 access_token", required = true)})
    public ResponseEntity<?> saveAnswer(@AuthenticationPrincipal User user,
                                        @PathVariable Long chatRoomId,
                                        @RequestBody AnswerRequestDto answerRequestDto) throws Exception {
        Answer answer = interViewService.saveAnswer(user, answerRequestDto, chatRoomId);
        Consulting consulting = consultingService.processConsulting(answer);
        interViewService.consultingInjection(answer, consulting);

        log.info("[Answer 저장] User: {}, Answer: {}, consulting: {}", user, answerRequestDto.answerContent(), consulting.getFeedback());
        return ResponseEntity.status(HttpStatus.CREATED).body("답변을 성공적으로 저장하였습니다.");
    }

    @GetMapping("/chat-rooms/{chatRoomId}/questions")
    @Parameters({@Parameter(name = "Authorization", description = "로그인 성공 후 발급 받은 access_token", required = true)})
    public ResponseEntity<?> getQuestion() {
        Question randomQuestion = interViewService.getRandomQuestion();
        log.info("[Question 반환] Question: {}", randomQuestion);
        return ResponseEntity.status(HttpStatus.OK).body(randomQuestion);
    }

    @DeleteMapping("/chat-rooms/{chatRoomId}")
    @Parameters({@Parameter(name = "Authorization", description = "로그인 성공 후 발급 받은 access_token", required = true)})
    public ResponseEntity<?> deleteChatRoom(@AuthenticationPrincipal User user,
                                            @PathVariable Long chatRoomId) {
        interViewService.deleteChatRoom(user, chatRoomId);
        log.info("[ChatRoom 삭제] User: {}, chatRoomId: {}", user, chatRoomId);
        return ResponseEntity.status(HttpStatus.OK).body("채팅방을 성공적으로 삭제하였습니다.");
    }














}
