package org.v1.job_coach.domain.chat.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.v1.job_coach.domain.chat.application.ChatMessageService;
import org.v1.job_coach.domain.chat.domain.ChatMessage;
import org.v1.job_coach.domain.chat.dto.ChatMessageDto;
import org.v1.job_coach.domain.chat.dto.ChattingRoomCreateRequestDto;
import org.v1.job_coach.domain.chat.dto.ChattingRoomCreateResponseDto;
import org.v1.job_coach.domain.chat.application.ChattingRoomService;

@Slf4j
@Tag(name = "ChatRoom", description = "모의면접 API")
@RestController
@RequestMapping("/api/v1/chattingRoom")
public class ChattingRoomController {
    private final ChattingRoomService chattingRoomService;
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChattingRoomController(ChattingRoomService chattingRoomService, ChatMessageService chatMessageService, SimpMessagingTemplate messagingTemplate) {
        this.chattingRoomService = chattingRoomService;
        this.chatMessageService = chatMessageService;
        this.messagingTemplate = messagingTemplate;
    }
    @PostMapping("/personal") //개인 DM 채팅방 생성
    @Operation(summary = "코치와 채팅 생성 API", description = "코치와 1:1 채팅방을 생성하는 API입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ChattingRoomCreateResponseDto createPersonalChatRoom(@RequestBody ChattingRoomCreateRequestDto request) {
        log.info("[ChattingRoom 생성]user:{}, coach:{}", request.userId(), request.coachId());
        return chattingRoomService.createChatRoomForPersonal(request);
    }

    @MessageMapping("/message")
    @Operation(summary = "메세지 생성 API", description = "메세지를 생성하는 API입니다.")
    public void sendMessage(ChatMessageDto message) {
        log.info("[ChatMessageController] sendMessage 메소드 진입 - Room ID: {}, Author ID: {}, Message: {}", message.roomId(), message.authorId(), message.message());

        // 실시간으로 방에서 채팅하기
        ChatMessage newChat = chatMessageService.createChatMessage(message);
        log.info("received message: {}", message);

        // 방에 있는 모든 사용자에게 메시지 전송
        messagingTemplate.convertAndSend("/sub/channel/"+message.roomId(), newChat);
    }
}
