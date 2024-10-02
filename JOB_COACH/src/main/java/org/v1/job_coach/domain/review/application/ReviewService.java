package org.v1.job_coach.domain.review.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.v1.job_coach.domain.review.dto.request.ReviewRequestDto;
import org.v1.job_coach.domain.review.dto.response.ReviewDetailResponseDto;
import org.v1.job_coach.domain.review.dto.response.ReviewResponseDto;

public interface ReviewService {

    ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto, Long userId);
    void updateReview(Long reviewId, ReviewRequestDto reviewRequestDto, Long userId);//리뷰 작성
    Page<ReviewResponseDto> getAllReviews(Pageable pageable); //리뷰 10개씩 페이징처리 조회
    void deleteReview(Long id, Long userId); //리뷰 삭제
    ReviewDetailResponseDto getReviewById(Long id); //리뷰 작성번호로 조회
    Page<ReviewResponseDto> searchReviews(Pageable pageable, String title);

}
