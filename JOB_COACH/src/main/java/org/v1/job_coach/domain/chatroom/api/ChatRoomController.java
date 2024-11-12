package org.v1.job_coach.domain.chatroom.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.v1.job_coach.domain.answer.dto.request.AnswerRequestDto;
import org.v1.job_coach.domain.chatroom.application.ChatRoomService;
import org.v1.job_coach.domain.chatroom.dto.response.AnswerResponseDto;
import org.v1.job_coach.domain.consulting.application.ConsultingService;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.user.domain.User;

@Slf4j
@Tag(name = "Interview", description = "모의면접 API")
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
    @PostMapping("/interview-rooms")
    @Operation(summary = "모의면접 생성 API", description = "모의면접 채팅방을 생성하는 API입니다.")
    public ResponseEntity<?> createChatRoom(@AuthenticationPrincipal User user,
                                            @RequestParam String roomName) {
        ResultResponseDto<?> chatRoomDto = chatRoomService.createChatRoom(user, roomName);
        log.info("[ChatRoom 생성] user: {}, roomName: {}", user, roomName);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomDto);
    }
    public ResultResponseDto<?> getChatRoom(@AuthenticationPrincipal User user, @PathVariable Long roomId) {
        return chatRoomService.getChatRoom(user, roomId);
    }

    @GetMapping("/interview-rooms/{roomId}")
    @Operation(summary = "하나의 모의면접 조회 API", description = "하나의 모의면접 채팅방을 조회하는 API입니다.")
    public ResponseEntity<?> createChatRoom(@AuthenticationPrincipal User user,
                                            @PathVariable Long roomId) {
        ResultResponseDto<?> chatRoomDto = chatRoomService.getChatRoom(user, roomId);
        log.info("[ChatRoom 조회] roomId: {}", roomId);
        return ResponseEntity.status(HttpStatus.OK).body(chatRoomDto);
    }

    @PutMapping("/interview-rooms/{roomId}/deactivate")
    @Operation(summary = "모의면접 종료 API", description = "모의면접 채팅을 종료하는 API입니다.")
    public ResponseEntity<?> endChatRoom(@AuthenticationPrincipal User user,
                                         @PathVariable Long roomId) {
        ResultResponseDto<?> resultResponseDto = chatRoomService.deactivateChatRoom(user, roomId);
        log.info("[ChatRoom 종료] roomId: {}", roomId);
        return ResponseEntity.status(HttpStatus.OK).body(resultResponseDto);
    }

    @PostMapping("/interview-rooms/{roomId}/answers")
    @Operation(summary = "모의면접 답변 저장 API", description = "모의면접 질문에 응답한 사용자 답변을 저장하는 API입니다.")
    public ResponseEntity<?> saveAnswer(@AuthenticationPrincipal User user,
                                        @PathVariable Long roomId,
                                        @RequestBody AnswerRequestDto answerRequestDto) throws Exception {

        // 서비스 호출을 통해 Answer와 Consulting 모두 저장
        AnswerResponseDto answerResponseDto = chatRoomService.saveAnswer(user, answerRequestDto, roomId);

        // 응답 메시지 생성
        ResultResponseDto<?> saveAnswerResponseDto = chatRoomService.toSaveAnswerResponseDto(answerResponseDto.id());
        log.info("[Answer 저장] User: {}, Answer: {}", user, answerRequestDto.answerContent());

        return ResponseEntity.status(HttpStatus.CREATED).body(saveAnswerResponseDto);
    }

    @GetMapping("/interview-rooms/{roomId}/questions")
    @Operation(summary = "모의면접 질문 반환 API", description = "모의면접 질문을 반환하는 API입니다.")
    public ResponseEntity<?> getQuestion(@AuthenticationPrincipal User user, @PathVariable Long roomId) {

        log.info("채빙탕 ID {} -> 질문 요청", roomId);
        ResultResponseDto<?> question = chatRoomService.getQuestion(user, roomId);
        log.info("[Question 반환] Question: {}", question.data());
        return ResponseEntity.status(HttpStatus.OK).body(question);
    }

    @DeleteMapping("/interview-rooms/{roomId}")
    @Operation(summary = "모의면접 채팅방 삭제 API", description = "모의면접 채팅방을 삭제하는 API입니다.")
    public ResponseEntity<?> deleteChatRoom(@AuthenticationPrincipal User user,
                                            @PathVariable Long roomId) {

        ResultResponseDto<?> resultResponseDto = chatRoomService.deleteChatRoom(user, roomId);
        log.info("[ChatRoom 삭제] User: {}, roomId: {}", user, roomId);

        return ResponseEntity.status(HttpStatus.OK).body(resultResponseDto);
    }














}
