package org.v1.job_coach.domain.review.dto.response;

import org.v1.job_coach.coach.domain.Expertise;
import org.v1.job_coach.domain.review.domain.Evaluation;
import org.v1.job_coach.domain.review.domain.Review;
import org.v1.job_coach.domain.review.domain.WorkExpertise;
import org.v1.job_coach.global.util.DateFormatter;

import java.text.DateFormat;
import java.time.LocalDate;

public record ReviewResponseDto(
        String title,
        String companyName,
        String evaluation,
        String result,
        String userName,
        String createDate,
        Expertise expertise,
        WorkExpertise workExpertise,
        String interviewDate
)
{
    public static ReviewResponseDto toDto(Review review) {
        return new ReviewResponseDto(
                review.getTitle(),
                review.getCompanyName(),
                String.valueOf(review.getEvaluation()),
                String.valueOf(review.getResult()),
                review.getUser().getName(),
                DateFormatter.formatDate(review.getCreateDate().toLocalDate()), // LocalDate 형식으로 포맷
                review.getExpertise(),
                review.getWorkExpertise(),
                DateFormatter.formatDate(review.getInterviewDate().toLocalDate()) // LocalDate 형식으로 포맷
        );
    }
}
