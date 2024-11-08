/*
package org.v1.job_coach.domain.chat.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.v1.job_coach.domain.chat.dto.ChatMessageDto;
import org.v1.job_coach.domain.chat.application.ChatMessageService;
import org.v1.job_coach.domain.chat.application.ChattingRoomService;
import org.v1.job_coach.domain.chat.domain.ChatMessage;

@Slf4j
@RestController
@Tag(name = "ChatRoom", description = "모의면접 API")
@RequestMapping("/api/v1/chattingRoom")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final ChattingRoomService chattingRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessageController(ChatMessageService chatMessageService, ChattingRoomService chattingRoomService, SimpMessagingTemplate messagingTemplate) {
        this.chatMessageService = chatMessageService;
        this.chattingRoomService = chattingRoomService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message")
    public void sendMessage(ChatMessageDto message) {
        log.info("[ChatMessageController] sendMessage 메소드 진입 - Room ID: {}, Author ID: {}, Message: {}", message.roomId(), message.authorId(), message.message());

        // 실시간으로 방에서 채팅하기
        ChatMessage newChat = chatMessageService.createChatMessage(message);
        log.info("received message: {}", message);

        // 방에 있는 모든 사용자에게 메시지 전송
        messagingTemplate.convertAndSend("/sub/channel/"+message.roomId(), newChat);
    }
}
*/
