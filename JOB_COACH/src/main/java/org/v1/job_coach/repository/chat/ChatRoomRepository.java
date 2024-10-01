package org.v1.job_coach.repository.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.entity.chat.ChatRoom;
import org.v1.job_coach.entity.chat.Question;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Page<ChatRoom> findById(Long id, Pageable pageable);
    Page<ChatRoom> findChatRoomsByUserPid(Long userId, Pageable pageable);

}
