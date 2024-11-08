package org.v1.job_coach.domain.chatroom.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.v1.job_coach.domain.chatroom.domain.ChatRoomQuestion;

public interface ChatRoomQuestionRepository extends JpaRepository<ChatRoomQuestion, Long> {
    Page<ChatRoomQuestion> findByChatRoom_Id(Long chatRoomId, Pageable pageable);
}
