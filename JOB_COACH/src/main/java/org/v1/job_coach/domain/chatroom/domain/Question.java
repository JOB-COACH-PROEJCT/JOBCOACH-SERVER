package org.v1.job_coach.domain.chatroom.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.v1.job_coach.domain.answer.domain.Answer;
import org.v1.job_coach.domain.consulting.domain.Consulting;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @OneToMany(mappedBy = "question")
    private List<ChatRoomQuestion> chatRoomQuestions = new ArrayList<>();

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consulting> consulting;

    public Question(String content) {
        this.content = content;
        this.answers = new ArrayList<>();
        this.consulting = new ArrayList<>();
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.theQuestion(this); // 양방향 연관관계 설정. 공부해 */
    }
}
