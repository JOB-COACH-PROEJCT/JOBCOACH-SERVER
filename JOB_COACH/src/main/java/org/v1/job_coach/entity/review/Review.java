package org.v1.job_coach.entity.review;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.v1.job_coach.dto.review.RequestReviewDto;
import org.v1.job_coach.entity.User;

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
    private String tips;   // 면접 TIP

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 작성한 사용자

    public Review(RequestReviewDto requestReviewDto, User user){
        this.title = requestReviewDto.title();
        this.companyName = requestReviewDto.companyName();
        this.evaluation = Evaluation.valueOf(requestReviewDto.evaluation());
        this.result = ReviewResult.valueOf(requestReviewDto.result());
        this.process = requestReviewDto.process();
        this.interviewQuestions = requestReviewDto.interviewQuestions();
        this.createDate = LocalDateTime.now();
        this.tips = requestReviewDto.tips();
        this.user = user;
    }

    public Review(Long id, String title, String companyName, LocalDateTime createDate){
        this.id = id;
        this.title = title;
        this.companyName = companyName;
        this.createDate = createDate;
        this.createDate = LocalDateTime.now();
    }

    public void update(RequestReviewDto requestReviewDto) {
        this.title = requestReviewDto.title();
        this.companyName = requestReviewDto.companyName();
        this.evaluation = Evaluation.valueOf(requestReviewDto.evaluation());
        this.result = ReviewResult.valueOf(requestReviewDto.result());
        this.process = requestReviewDto.process();
        this.interviewQuestions = requestReviewDto.interviewQuestions();
        this.tips = requestReviewDto.tips();
    }
}
