package org.v1.job_coach.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.entity.chat.ChatRoom;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findChatRoomByUser(User user);
}
