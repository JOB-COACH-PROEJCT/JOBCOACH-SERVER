package org.v1.job_coach.entity.chat;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Consulting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Answer answer;

    private String feedback; //컨설팅 결과
}
