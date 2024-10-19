package org.v1.job_coach.domain.chat.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.v1.job_coach.coach.domain.Coach;
import org.v1.job_coach.domain.chat.domain.ChattingRoom;
import org.v1.job_coach.user.domain.User;

import java.util.Optional;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {
    Optional<ChattingRoom> findById(String id);

    Optional<ChattingRoom> findByChatRoomMembersContainingAndChatRoomMembersContaining(User user, Coach coach);
}
