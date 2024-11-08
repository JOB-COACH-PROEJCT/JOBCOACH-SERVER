package org.v1.job_coach.domain.review.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.v1.job_coach.domain.review.dto.request.ReviewRequestDto;
import org.v1.job_coach.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(min = 2, max = 20, message = "2자 이상, 20자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "기업명은 필수 항목입니다.")
    private String companyName;   // 기업명

    @Enumerated(EnumType.STRING)
    private Evaluation evaluation;  // 면접 평가 (긍정, 평이, 부정)

    @Enumerated(EnumType.STRING)
    private ReviewResult result;  // 결과 (합격, 불합격)

    @Lob
    @NotBlank(message = "내용을 입력해주세요.")
    private String process;   // 진행 방식

    @NotEmpty(message = "면접 질문을 하나 이상 입력해주세요.")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "review_questions", joinColumns = @JoinColumn(name = "review_id"))
    private List<String> interviewQuestions;   // 면접 질문

    @Column
    private LocalDateTime createDate;

    @Lob
    @NotBlank(message = "내용을 입력해주세요.")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String tips;   // 면접 TIP

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 작성한 사용자

    public Review(ReviewRequestDto reviewRequestDto, User user){
        this.title = reviewRequestDto.title();
        this.companyName = reviewRequestDto.companyName();
        this.evaluation = Evaluation.valueOf(reviewRequestDto.evaluation());
        this.result = ReviewResult.valueOf(reviewRequestDto.result());
        this.process = reviewRequestDto.process();
        this.interviewQuestions = reviewRequestDto.interviewQuestions();
        this.createDate = LocalDateTime.now();
        this.tips = reviewRequestDto.tips();
        this.user = user;
    }

    public Review(Long id, String title, String companyName, LocalDateTime createDate){
        this.id = id;
        this.title = title;
        this.companyName = companyName;
        this.createDate = createDate;
        this.createDate = LocalDateTime.now();
    }

    public void update(ReviewRequestDto reviewRequestDto) {
        this.title = reviewRequestDto.title();
        this.companyName = reviewRequestDto.companyName();
        this.evaluation = Evaluation.valueOf(reviewRequestDto.evaluation());
        this.result = ReviewResult.valueOf(reviewRequestDto.result());
        this.process = reviewRequestDto.process();
        this.interviewQuestions = reviewRequestDto.interviewQuestions();
        this.tips = reviewRequestDto.tips();
    }
}
