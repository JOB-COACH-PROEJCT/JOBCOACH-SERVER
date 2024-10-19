package org.v1.job_coach.domain.chat.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v1.job_coach.coach.dao.CoachRepository;
import org.v1.job_coach.coach.dao.MatchingRepository;
import org.v1.job_coach.coach.domain.Coach;
import org.v1.job_coach.coach.domain.Matching;
import org.v1.job_coach.coach.domain.MatchingStatus;
import org.v1.job_coach.domain.chat.dao.ChattingRoomRepository;
import org.v1.job_coach.domain.chat.domain.ChattingRoom;
import org.v1.job_coach.domain.chat.dto.ChattingRoomCreateRequestDto;
import org.v1.job_coach.domain.chat.dto.ChattingRoomCreateResponseDto;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.global.util.SecurityUtil;
import org.v1.job_coach.user.dao.UserRepository;
import org.v1.job_coach.user.domain.User;

import java.util.Optional;

@Slf4j
@Service
public class ChattingRoomServiceImpl implements ChattingRoomService {
    private final UserRepository userRepository;
    private final CoachRepository coachRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final MatchingRepository matchingRepository;

    public ChattingRoomServiceImpl(UserRepository userRepository, ChattingRoomRepository chattingRoomRepository, CoachRepository coachRepository, MatchingRepository matchingRepository) {
        this.userRepository = userRepository;
        this.chattingRoomRepository = chattingRoomRepository;
        this.coachRepository = coachRepository;
        this.matchingRepository = matchingRepository;
    }

    @Transactional
    @Override // 개인 DM방 생성
        public ChattingRoomCreateResponseDto createChatRoomForPersonal(ChattingRoomCreateRequestDto requestDto) {
        Long currentMemberUserPid = SecurityUtil.getCurrentMemberUserPid();
        log.info("현재 사용자 ID: {}, 방 생성자 ID: {}, 게스트 ID: {}", currentMemberUserPid, requestDto.userId(), requestDto.coachId());

        if (!currentMemberUserPid.equals(requestDto.userId())) {
            throw new CustomException(Error.NOT_FOUND_USER);
        }

        // 1. 사용자와 Coach의 Matching 여부 확인
        User requestUser = getUser(requestDto);
        Coach requestCoach = getCoach(requestDto);
        Matching matching = getMatching(requestUser, requestCoach);

        if (!matching.getStatus().equals(MatchingStatus.APPROVED)) {
            throw new CustomException(Error.MATCHING_NOT_APPROVED);
        }

        // 2. 매칭된 사용자와 Coach의 채팅방 존재 여부 확인
        if (matching.getChattingRoom() != null) {
            ChattingRoom existingRoom = matching.getChattingRoom();
            log.info("기존 채팅방이 존재합니다. Room ID: {}", existingRoom.getId());
            return new ChattingRoomCreateResponseDto(requestDto.userId(), requestDto.coachId(), existingRoom.getId());
        }

        // 3. 채팅방이 없으면 새로 생성
        ChattingRoom newRoom = ChattingRoom.create();
        log.info("새 채팅방에 멤버 추가 시작");
        newRoom.addMembers(requestUser, requestCoach);

        // 명시적으로 영속화
        userRepository.save(requestUser);
        coachRepository.save(requestCoach);

        chattingRoomRepository.save(newRoom);

        // 4. 생성된 채팅방을 매칭에 연결
        matching.createChattingRoom(newRoom);
        matchingRepository.save(matching);

        return new ChattingRoomCreateResponseDto(requestUser.getPid(), requestCoach.getPid(), newRoom.getId());
    }

    private Optional<ChattingRoom> getChattingRoom(User user, Coach coach) {
        return chattingRoomRepository.findByChatRoomMembersContainingAndChatRoomMembersContaining(user, coach);
    }

    private Matching getMatching(User requestUser, Coach requestCoach) {
        Matching matching = matchingRepository.findByUser_PidAndCoach_Pid(requestUser.getPid(), requestCoach.getPid()).orElseThrow(() -> new CustomException(Error.NOT_FOUND_CHATROOM));
        return matching;
    }

    private Coach getCoach(ChattingRoomCreateRequestDto requestDto) {
        Coach requestCoach = coachRepository.findById(requestDto.coachId()).orElseThrow(() -> new CustomException(Error.NOT_FOUND_COACH));
        return requestCoach;
    }

    private User getUser(ChattingRoomCreateRequestDto requestDto) {
        User requestUser = userRepository.findById(requestDto.userId()).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
        return requestUser;
    }
}
