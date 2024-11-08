package org.v1.job_coach.domain.review.application.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v1.job_coach.domain.review.application.ReviewService;
import org.v1.job_coach.domain.review.dao.ReviewRepository;
import org.v1.job_coach.domain.review.domain.Review;
import org.v1.job_coach.domain.review.dto.request.ReviewRequestDto;
import org.v1.job_coach.domain.review.dto.response.ReviewDetailResponseDto;
import org.v1.job_coach.domain.review.dto.response.ReviewResponseDto;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.user.dao.UserRepository;
import org.v1.job_coach.user.domain.User;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(UserRepository userRepository, ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }
    @Override
    @Transactional
    public ResultResponseDto<?> createReview(ReviewRequestDto reviewRequestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
        Review review = new Review(reviewRequestDto, user);
        reviewRepository.save(review);
        return ResultResponseDto.toResultResponseDto(
                201,
                "성공적으로 면접 후기를 저장하였습니다."
        );
    }
    @Override
    @Transactional
    public ResultResponseDto<?> updateReview(Long reviewId, ReviewRequestDto reviewRequestDto, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
        Review review = isReviewPresent(reviewId);
        //작성자와 다를시 오류
        extracted(userId, review);

        review.update(reviewRequestDto);
        return ResultResponseDto.toResultResponseDto(
                201,
                "성공적으로 면접 후기를 수정하였습니다."
        );
    }

    @Override
    @Transactional
    public ResultResponseDto<Page<?>> getAllReviews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Review> reviews = reviewRepository.findAll(pageable);

        if (page >= reviews.getTotalPages()){
            throw new CustomException(Error.INVALID_PAGE);
        }

        return ResultResponseDto.toDataResponseDto(
                200,
                "면접 후기 목록을 반환합니다.",
                reviews.map(ReviewResponseDto::toDto)
        );
    }

    @Override
    @Transactional
    public ResultResponseDto<?> deleteReview(Long id, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
        Review review = isReviewPresent(id);

        //작성자와 다를시 오류
        extracted(userId, review);
        reviewRepository.delete(review);

        return ResultResponseDto.toResultResponseDto(
                200,
                "성공적으로 면접 후기를 삭제하였습니다."
        );
    }
    @Override
    @Transactional
    public ResultResponseDto<?> getReviewById(User user, Long id) {

        isLogin(user);
        return ResultResponseDto.toDataResponseDto(
                200,
                "면접 후기 목록을 반환합니다.",
                ReviewDetailResponseDto.toDto(isReviewPresent(id)
                ));
    }

    @Transactional
    public ResultResponseDto<Page<?>> searchReviews(Pageable pageable, String title) {
        List<Review> allReviews = reviewRepository.findAll();

        List<Review> filteredReviews = allReviews.stream()
                .filter(review -> review.getTitle().contains(title))
                .toList();
        int start = Math.min((int) pageable.getOffset(), filteredReviews.size());
        int end = Math.min(start + pageable.getPageSize(), filteredReviews.size());


        List<ReviewResponseDto> reviewDtos = filteredReviews.subList(start, end)
                .stream()
                .map(ReviewResponseDto::toDto)
                .collect(Collectors.toList());

        // 페이지 범위 초과 예외 처리
        if (start >= filteredReviews.size()) {
            throw new CustomException(Error.INVALID_PAGE);
        }

        if (reviewDtos.isEmpty()) {
            return ResultResponseDto.toResultResponseDto(
                    204,
                    "해당 검색 조건에 일치하는 결과가 없습니다.");
        }

        return ResultResponseDto.toDataResponseDto(
                200,
                "면접 후기 목록을 반환합니다.",
                new PageImpl<>(reviewDtos, pageable, filteredReviews.size()));
    }

    private Review isReviewPresent(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(Error.NOT_FOUND_REVIEW));
    }

    private static void extracted(Long userId, Review review) {
        if (!review.getUser().getPid().equals(userId)) {
            throw new CustomException(Error.NOT_AUTHORIZED);
        }
    }

    private void isLogin(User user) {
        userRepository.findById(user.getPid()).orElseThrow(() -> new CustomException(Error.ACCESS_DENIED));
    }


}
