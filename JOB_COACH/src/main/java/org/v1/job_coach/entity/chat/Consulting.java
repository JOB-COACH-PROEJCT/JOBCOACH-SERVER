package org.v1.job_coach.entity.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(length = 1000)
    private String feedback; //컨설팅 결과

    public Consulting(Answer answer, String feedback) {
        this.answer = answer;
        this.feedback = feedback;
    }
}
