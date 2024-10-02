package org.v1.job_coach.domain.review.dto.response;

import org.v1.job_coach.domain.review.domain.Review;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewDetailResponseDto(
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
    public static ReviewDetailResponseDto toDto(Review review) {
        return new ReviewDetailResponseDto(
                review.getTitle(),
                review.getCompanyName(),
                String.valueOf(review.getEvaluation()),
                String.valueOf(review.getResult()),
                review.getProcess(),
                review.getInterviewQuestions(),
                review.getTips(),
                review.getUser().getName(),
                review.getCreateDate());
    }
}