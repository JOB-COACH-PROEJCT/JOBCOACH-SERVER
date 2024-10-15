package org.v1.job_coach.domain.chat.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.v1.job_coach.domain.chat.dao.ChattingRoomRepository;
import org.v1.job_coach.domain.chat.domain.ChattingRoom;
import org.v1.job_coach.domain.chat.dto.ChattingRoomCreateRequestDto;
import org.v1.job_coach.domain.chat.dto.ChattingRoomCreateResponseDto;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.user.dao.UserRepository;
import org.v1.job_coach.user.domain.User;
import org.v1.job_coach.global.util.SecurityUtil;

@Slf4j
@Service
public class ChattingRoomServiceImpl implements ChattingRoomService {
    private final UserRepository userRepository;
    private final ChattingRoomRepository chattingRoomRepository;

    public ChattingRoomServiceImpl(UserRepository userRepository, ChattingRoomRepository chatRoomRepository, ChattingRoomRepository chattingRoomRepository, SecurityUtil securityUtil) {
        this.userRepository = userRepository;
        this.chattingRoomRepository = chattingRoomRepository;
    }

    @Override // 개인 DM방 생성
        public ChattingRoomCreateResponseDto createChatRoomForPersonal(ChattingRoomCreateRequestDto requestDto) {

        Long currentMemberUserPid = SecurityUtil.getCurrentMemberUserPid(); //id=roomMakerId 같아야,,,하겟지?
        log.info("현재 사용자 ID: {}, 방 생성자 ID: {}, 게스트 ID: {}", currentMemberUserPid, requestDto.roomMakerId(), requestDto.guestId());

        if (!String.valueOf(currentMemberUserPid).equals(requestDto.roomMakerId())) {
            throw new CustomException(Error.NOT_FOUND_USER);
        }
        User roomMaker = userRepository.findById(currentMemberUserPid).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
        User guest = userRepository.findById(Long.valueOf(requestDto.guestId())).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        ChattingRoom newRoom = ChattingRoom.create();
        newRoom.addMembers(roomMaker, guest);

        chattingRoomRepository.save(newRoom);

        return new ChattingRoomCreateResponseDto(roomMaker.getPid(), guest.getPid(), newRoom.getId());
    }
}
