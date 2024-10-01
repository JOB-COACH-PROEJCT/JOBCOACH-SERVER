package org.v1.job_coach.domain.review.application.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v1.job_coach.domain.review.application.ReviewService;
import org.v1.job_coach.domain.review.dao.ReviewRepository;
import org.v1.job_coach.domain.review.domain.Review;
import org.v1.job_coach.domain.review.dto.request.ReviewRequestDto;
import org.v1.job_coach.domain.review.dto.response.ReviewResponseDto;
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
    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
        Review review = new Review(reviewRequestDto, user);
        reviewRepository.save(review);
        return ReviewResponseDto.toDto(review);
    }
    @Override
    @Transactional
    public void updateReview(Long reviewId, ReviewRequestDto reviewRequestDto, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(Error.NOT_FOUND_REVIEW));
        //작성자와 다를시 오류
        if (!review.getUser().getPid().equals(userId)) {
            throw new CustomException(Error.NOT_AUTHORIZED);
        }
        review.update(reviewRequestDto);
    }
    @Override
    @Transactional
    public Page<ReviewResponseDto> getAllReviews(Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAll(pageable);
        return reviews.map(ReviewResponseDto::toDto);
    }
    @Override
    @Transactional
    public void deleteReview(Long id, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
        Review review = reviewRepository.findById(id).orElseThrow(() -> new CustomException(Error.NOT_FOUND_REVIEW));

        if (!review.getUser().getPid().equals(userId)) {
            throw new CustomException(Error.NOT_AUTHORIZED);
        }
        reviewRepository.delete(review);
    }
    @Override
    public ReviewResponseDto getReviewById(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new CustomException(Error.NOT_FOUND_REVIEW));
        return ReviewResponseDto.toDto(review);
    }
    public Page<ReviewResponseDto> searchReviews(Pageable pageable, String title) {
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
        return new PageImpl<>(reviewDtos, pageable, filteredReviews.size());
    }

}
