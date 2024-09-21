package org.v1.job_coach.service.review.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v1.job_coach.dto.review.RequestReviewDto;
import org.v1.job_coach.dto.review.ResponseReviewDto;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.entity.review.Review;
import org.v1.job_coach.exception.CustomException;
import org.v1.job_coach.exception.Error;
import org.v1.job_coach.repository.UserRepository;
import org.v1.job_coach.repository.review.ReviewRepository;
import org.v1.job_coach.service.review.ReviewService;

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
    public ResponseReviewDto createReview(RequestReviewDto requestReviewDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
        Review review = new Review(requestReviewDto, user);
        reviewRepository.save(review);
        return ResponseReviewDto.toDto(review);
    }
    @Override
    @Transactional
    public void updateReview(Long reviewId, RequestReviewDto requestReviewDto, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(Error.NOT_FOUND_REVIEW));
        //작성자와 다를시 오류
        if (!review.getUser().getPid().equals(userId)) {
            throw new CustomException(Error.NOT_AUTHORIZED);
        }
        review.update(requestReviewDto);
    }
    @Override
    @Transactional
    public Page<ResponseReviewDto> getAllReviews(Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAll(pageable);
        return reviews.map(ResponseReviewDto::toDto);
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
    public ResponseReviewDto getReviewById(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new CustomException(Error.NOT_FOUND_REVIEW));
        return ResponseReviewDto.toDto(review);
    }
    public Page<ResponseReviewDto> searchReviews(Pageable pageable, String title) {
        List<Review> allReviews = reviewRepository.findAll();

        List<Review> filteredReviews = allReviews.stream()
                .filter(review -> review.getTitle().contains(title))
                .toList();
        int start = Math.min((int) pageable.getOffset(), filteredReviews.size());
        int end = Math.min(start + pageable.getPageSize(), filteredReviews.size());
        List<ResponseReviewDto> reviewDtos = filteredReviews.subList(start, end)
                .stream()
                .map(ResponseReviewDto::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(reviewDtos, pageable, filteredReviews.size());
    }

}
