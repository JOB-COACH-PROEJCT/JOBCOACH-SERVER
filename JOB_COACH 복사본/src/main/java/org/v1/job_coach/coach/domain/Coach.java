package org.v1.job_coach.coach.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.v1.job_coach.coach.dto.CoachSignUpRequestDto;
import org.v1.job_coach.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED) /*JPA에서 상속 관계를 매핑할 때 사용하는 어노테이션*/
public class Coach extends User {

    private String university;

    @Lob()
    @Column(columnDefinition = "TEXT")
    private String career;

    @Lob()
    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Enumerated(EnumType.STRING)
    Expertise expertise;

    @Enumerated(EnumType.STRING)
    AvailableTimes availableTimes;

    // private List<CoachReview> coachReviewList; -> 추후 추가

    // 명확한 필드를 포함한 생성자 추가
    public Coach(boolean isActive, String email, String number, String password, String name, String profile,
                 List<String> roles) {
        super(isActive, email, number, password, name, profile, roles);
    }

    public void updateCoachDetails(CoachSignUpRequestDto coachSignUpRequestDto) {
        this.career = coachSignUpRequestDto.career();
        this.university = coachSignUpRequestDto.university();
        this.introduction = coachSignUpRequestDto.introduction();
        this.expertise = Expertise.valueOf(coachSignUpRequestDto.expertise());
        this.availableTimes = AvailableTimes.valueOf(coachSignUpRequestDto.availableTimes());
    }
}
