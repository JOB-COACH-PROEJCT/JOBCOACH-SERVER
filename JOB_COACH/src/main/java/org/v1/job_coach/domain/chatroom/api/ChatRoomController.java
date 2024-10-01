package org.v1.job_coach.domain.chatroom.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.v1.job_coach.domain.answer.domain.Answer;
import org.v1.job_coach.domain.answer.dto.request.AnswerRequestDto;
import org.v1.job_coach.domain.chatroom.application.ChatRoomService;
import org.v1.job_coach.domain.consulting.application.ConsultingService;
import org.v1.job_coach.domain.consulting.domain.Consulting;
import org.v1.job_coach.user.domain.User;

@Slf4j
@Tag(name = "ChatRoom", description = "모의면접 API")
@RestController
@RequestMapping("/api/v1/interview")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ConsultingService consultingService;

    public ChatRoomController(ChatRoomService chatRoomService, ConsultingService consultingService) {
        this.chatRoomService = chatRoomService;

        this.consultingService = consultingService;
    }
    /* 모의 면접 채팅방을 모두 반환하는 컨트롤러 생성해야 함 */

    @PostMapping("/chat-rooms")
    @Operation(summary = "모의면접 생성 API", description = "모의면접 채팅방을 생성하는 API입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true),})
    public ResponseEntity<?> createChatRoom(@AuthenticationPrincipal User user,
                                            @RequestParam String roomName) {
        chatRoomService.createChatRoom(user, roomName);
        log.info("[ChatRoom 생성] user: {}, roomName: {}", user, roomName);
        return ResponseEntity.status(HttpStatus.CREATED).body("모의면접 채팅방을 성공적으로 생성하였습니다.");
    }
    @PutMapping("/chat-rooms/{chatRoomId}/deactivate")
    @Operation(summary = "모의면접 종료 API", description = "모의면접 채팅을 종료하는 API입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> endChatRoom(@AuthenticationPrincipal User user,
                                         @PathVariable Long chatRoomId) {
        chatRoomService.deactivateChatRoom(user, chatRoomId);
        log.info("[ChatRoom 종료] chatRoomId: {}", chatRoomId);
        return ResponseEntity.status(HttpStatus.OK).body("모의면접을 종료합니다.");
    }
    @PostMapping("/chat-rooms/{chatRoomId}/answers")
    @Operation(summary = "모의면접 답변 저장 API", description = "모의면접 질문에 응답한 사용자 답변을 저장하는 API입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> saveAnswer(@AuthenticationPrincipal User user,
                                        @PathVariable Long chatRoomId,
                                        @RequestBody AnswerRequestDto answerRequestDto) throws Exception {
        Answer answer = chatRoomService.saveAnswer(user, answerRequestDto, chatRoomId);
        Consulting consulting = consultingService.processConsulting(answer);
        //답변을 바탕으로 컨설팅 작업 -> 컨설팅 저장
        chatRoomService.consultingInjection(answer, consulting);
        log.info("[Answer 저장] User: {}, Answer: {}, consulting: {}", user, answerRequestDto.answerContent(), consulting.getFeedback());
        return ResponseEntity.status(HttpStatus.CREATED).body("답변을 성공적으로 저장하였습니다.");
    }
    @GetMapping("/chat-rooms/{chatRoomId}/questions")
    @Operation(summary = "모의면접 질문 반환 API", description = "모의면접 질문을 반환하는 API입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> getQuestion(@PathVariable Long chatRoomId) {
        log.info("채빙탕 ID {} -> 질문 요청", chatRoomId);
        log.info("[Question 반환] Question: {}", chatRoomService.getRandomQuestion());
        return ResponseEntity.status(HttpStatus.OK).body(chatRoomService.getRandomQuestion());
    }

    @DeleteMapping("/chat-rooms/{chatRoomId}")
    @Operation(summary = "모의면접 채팅방 삭제 API", description = "모의면접 채팅방을 삭제하는 API입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> deleteChatRoom(@AuthenticationPrincipal User user,
                                            @PathVariable Long chatRoomId) {
        chatRoomService.deleteChatRoom(user, chatRoomId);
        log.info("[ChatRoom 삭제] User: {}, chatRoomId: {}", user, chatRoomId);
        return ResponseEntity.status(HttpStatus.OK).body("채팅방을 성공적으로 삭제하였습니다.");
    }














}
