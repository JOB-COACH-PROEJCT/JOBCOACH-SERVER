package org.v1.job_coach.domain.review.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.v1.job_coach.domain.review.dto.request.ReviewRequestDto;
import org.v1.job_coach.domain.review.dto.response.ReviewDetailResponseDto;
import org.v1.job_coach.domain.review.dto.response.ReviewResponseDto;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.user.domain.User;

public interface ReviewService {

    ResultResponseDto<?> createReview(ReviewRequestDto reviewRequestDto, Long userId);
    ResultResponseDto<?> updateReview(Long reviewId, ReviewRequestDto reviewRequestDto, Long userId);//리뷰 작성
    ResultResponseDto<Page<?>> getAllReviews(Pageable pageable); //리뷰 10개씩 페이징처리 조회
    ResultResponseDto<?> deleteReview(Long id, Long userId); //리뷰 삭제
    ResultResponseDto<?> getReviewById(User user, Long id); //리뷰 작성번호로 조회
    ResultResponseDto<Page<?>> searchReviews(Pageable pageable, String title);

}
