package org.v1.job_coach.domain.chatroom.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoomQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public ChatRoomQuestion(ChatRoom chatRoom, Question question) {
        this.chatRoom = chatRoom;
        this.question = question;
    }
}
