package org.v1.job_coach.coach.domain;

import jakarta.persistence.*;
import org.v1.job_coach.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
/*JPA에서 상속 관계를 매핑할 때 사용하는 어노테이션*/
@Inheritance(strategy = InheritanceType.JOINED)
public class Coach extends User {

    @Lob()
    private String university;

    @Lob()
    @Column(nullable = false, columnDefinition = "TEXT")
    private String career;

    @Lob()
    @Column(nullable = false, columnDefinition = "TEXT")
    private String introduction;

    @Enumerated(EnumType.STRING)
    Expertise expertise;

    @Enumerated(EnumType.STRING)
    AvailableTimes availableTimes;

    // private List<CoachReview> coachReviewList; -> 추후 추가

/*    Coach(CoachRequest CoachREquest) {
        university
    }*/

}
