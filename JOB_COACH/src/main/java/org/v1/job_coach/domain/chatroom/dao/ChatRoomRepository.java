package org.v1.job_coach.domain.chatroom.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.v1.job_coach.domain.chatroom.domain.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Page<ChatRoom> findById(Long id, Pageable pageable);
    Page<ChatRoom> findChatRoomsByUserPid(Long userId, Pageable pageable);

}
