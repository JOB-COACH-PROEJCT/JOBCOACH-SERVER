package org.v1.job_coach.domain.answer.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.v1.job_coach.domain.chatroom.domain.ChatRoom;
import org.v1.job_coach.domain.chatroom.domain.Question;
import org.v1.job_coach.domain.consulting.domain.Consulting;
import org.v1.job_coach.user.domain.User;
@Entity
@Getter
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String content;

    @ManyToOne
    @JsonIgnore
    private ChatRoom chatRoom;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "consulting_id") //오류뜨면 얘때문에
    @JsonIgnore
    private Consulting consulting;

    public Answer(String content, ChatRoom chatRoom, Question question, User user) {
        this.content = content;
        this.chatRoom = chatRoom;
        this.question = question;
        this.user = user;
    }

    public void theQuestion(Question question) {
        this.question = question;
    }
    public void addConsulting(Consulting consulting) {
        this.consulting = consulting;
    }
}
