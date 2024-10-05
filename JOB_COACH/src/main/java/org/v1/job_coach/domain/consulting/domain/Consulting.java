package org.v1.job_coach.domain.consulting.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.v1.job_coach.domain.answer.domain.Answer;
import org.v1.job_coach.domain.chatroom.domain.ChatRoom;
import org.v1.job_coach.domain.chatroom.domain.Question;
import org.v1.job_coach.user.domain.User;
@Entity
@Getter
@NoArgsConstructor
public class Consulting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String feedback;

    public Consulting(Answer answer, String feedback) {
        this.answer = answer;
        this.feedback = feedback;
    }
    public Consulting(ChatRoom chatRoom, Question question, Answer answer, String feedback, User user) {
        this.chatRoom = chatRoom;
        this.question = question;
        this.answer = answer;
        this.feedback = feedback;
        this.user = user;
    }
}
