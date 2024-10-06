package org.v1.job_coach.domain.review.dto.response;

import lombok.Builder;
import org.v1.job_coach.domain.review.domain.Review;
import org.v1.job_coach.global.util.DateFormatter;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewResponseDto(
        String title,
        String companyName,
        String evaluation,
        String result,
        String userName,
        String createDate
)
{
    public static ReviewResponseDto toDto(Review review) {
    return new ReviewResponseDto(
            review.getTitle(),
            review.getCompanyName(),
            String.valueOf(review.getEvaluation()),
            String.valueOf(review.getResult()),
            review.getUser().getName(),
            DateFormatter.getDateNow(review.getCreateDate()));
    }
}
