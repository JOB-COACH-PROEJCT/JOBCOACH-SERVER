package org.v1.job_coach.domain.chat.application;

import org.v1.job_coach.domain.chat.dto.ChattingRoomCreateRequestDto;
import org.v1.job_coach.domain.chat.dto.ChattingRoomCreateResponseDto;

public interface ChattingRoomService {

    ChattingRoomCreateResponseDto createChatRoomForPersonal(ChattingRoomCreateRequestDto requestDto);
}
