package org.v1.job_coach.domain.chat.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.v1.job_coach.domain.chat.dto.ChattingRoomCreateRequestDto;
import org.v1.job_coach.domain.chat.dto.ChattingRoomCreateResponseDto;
import org.v1.job_coach.domain.chat.application.ChattingRoomService;

@RestController
@Slf4j
@RequestMapping("/api/v1/chattingRoom")
public class ChattingRoomController {
    private final ChattingRoomService chattingRoomService;

    public ChattingRoomController(ChattingRoomService chattingRoomService) {
        this.chattingRoomService = chattingRoomService;
    }
    @PostMapping("/personal") //개인 DM 채팅방 생성
    public ChattingRoomCreateResponseDto createPersonalChatRoom(@RequestBody ChattingRoomCreateRequestDto request) {
        log.info("[ChattingRoom 생성]roomMaker:{}, guest:{}", request.roomMakerId(), request.guestId());
        return chattingRoomService.createChatRoomForPersonal(request);
    }
}
