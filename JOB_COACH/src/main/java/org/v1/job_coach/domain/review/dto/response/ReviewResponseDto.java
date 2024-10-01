package org.v1.job_coach.domain.review.dto.response;

import lombok.Builder;
import org.v1.job_coach.domain.review.domain.Review;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReviewResponseDto(
        String title,
        String companyName,
        String evaluation,
        String result,
        String process,
        List<String> interviewQuestions,
        String tips,
        String userName,
        LocalDateTime createDate
) {
    public static ReviewResponseDto toDto(Review review) {
        return ReviewResponseDto.builder()
                .title(review.getTitle())
                .companyName(review.getCompanyName())
                .evaluation(String.valueOf(review.getEvaluation()))
                .result(String.valueOf(review.getResult()))
                .process(review.getProcess())
                .interviewQuestions(review.getInterviewQuestions())
                .tips(review.getTips())
                .userName(review.getUser().getName())
                .createDate(review.getCreateDate())
                .build();
    }
}
