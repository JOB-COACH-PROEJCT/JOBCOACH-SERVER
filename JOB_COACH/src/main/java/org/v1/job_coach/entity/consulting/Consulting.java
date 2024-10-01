package org.v1.job_coach.entity.consulting;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.entity.chat.Answer;
import org.v1.job_coach.entity.chat.ChatRoom;
import org.v1.job_coach.entity.chat.Question;

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

    @ManyToOne
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
