package org.v1.job_coach.domain.chat.application;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.v1.job_coach.domain.chat.dao.ChattingRoomRepository;
import org.v1.job_coach.domain.chat.domain.ChatMessage;
import org.v1.job_coach.domain.chat.domain.ChattingRoom;
import org.v1.job_coach.domain.chat.dto.ChatMessageDto;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.user.dao.UserRepository;
import org.v1.job_coach.user.domain.User;

@Slf4j
@Service
public class ChatMessageService {
    private final ChattingRoomRepository chattingRoomRepository;
    private final UserRepository userRepository;

    public ChatMessageService(ChattingRoomRepository chattingRoomRepository, UserRepository userRepository) {
        this.chattingRoomRepository = chattingRoomRepository;
        this.userRepository = userRepository;
    }

    public ChatMessage createChatMessage(ChatMessageDto chatMessageDto) {
        log.info("[ChatMessage] createChatMessage 실행 . . . .ChatMessage 생성 중");
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chatMessageDto.roomId()).orElseThrow(() -> new CustomException(Error.ACCESS_DENIED));

        User user = userRepository.findById(Long.valueOf(chatMessageDto.authorId())).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        if (!chattingRoom.isMember(user)) {
            log.warn("사용자가 채팅방에 속해 있지 않습니다. Room ID: {}, Author ID: {}", chatMessageDto.roomId(), chatMessageDto.authorId());
            throw new CustomException(Error.ACCESS_DENIED);
        }

        User author = userRepository.findById(Long.valueOf(chatMessageDto.authorId())).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        ChatMessage chatMessage = chatMessageDto.toEntity(author.getName(), author.getProfile());
        log.info("[ChatMessage] ChatMessage 생성 완료: {}", chatMessage.getId());
        log.info("[ChatMessage] ChatMessage 내용: {} , Author: {}", chatMessage.getMessage(), chatMessage.getAuthorId());
        chattingRoom.lastChatMsg(chatMessage);
        chattingRoomRepository.save(chattingRoom);
        log.info("[ChatMessage] ChatRoom 메세지 추가 후 저장 완료 . . .");

        return chatMessage;
    }
}
