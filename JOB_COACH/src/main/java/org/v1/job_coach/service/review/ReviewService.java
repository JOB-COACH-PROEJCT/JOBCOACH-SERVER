package org.v1.job_coach.service.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.v1.job_coach.dto.review.RequestReviewDto;
import org.v1.job_coach.dto.review.ResponseReviewDto;

public interface ReviewService {

    ResponseReviewDto createReview(RequestReviewDto requestReviewDto, Long userId);
    void updateReview(Long reviewId, RequestReviewDto requestReviewDto, Long userId);//리뷰 작성
    Page<ResponseReviewDto> getAllReviews(Pageable pageable); //리뷰 10개씩 페이징처리 조회
    void deleteReview(Long id, Long userId); //리뷰 삭제
    ResponseReviewDto getReviewById(Long id); //리뷰 작성번호로 조회
    Page<ResponseReviewDto> searchReviews(Pageable pageable, String title);

}
