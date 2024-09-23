package org.v1.job_coach.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.v1.job_coach.dto.review.RequestReviewDto;
import org.v1.job_coach.dto.review.ResponseReviewDto;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.exception.CustomException;
import org.v1.job_coach.exception.Error;
import org.v1.job_coach.exception.ErrorResponse;
import org.v1.job_coach.service.review.ReviewService;

import java.util.List;

@Slf4j
@Tag(name = "Reviews", description = "면접 후기 API")
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    //리뷰 조회
    @GetMapping()
    @Operation(summary = "작성된 면접 후기 목록 조회 API", description = "모든 회원이 작성한 면접 후기를 반환하는 API이며, 페이징을 포함합니다."
            + "query string으로 page 번호를 주세요(size는 선택, 기본 10)")
    @Parameters({@Parameter(name = "page", description = "페이지 번호, 0이상이어야 함, query string"),
            @Parameter(name = "size", description = "(선택적) 페이지당 컨텐츠 개수, 기본 10, query string")})
    public ResponseEntity<Page<ResponseReviewDto>> getReviews(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ResponseReviewDto> reviews = reviewService.getAllReviews(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping("/search")
    @Operation(summary = "면접 후기 조회 API", description = "제목에 검색한 키워드가 포함된 면접 후기를 반환하는 API이며, 페이징을 포함합니다."
            + "query string으로 page 번호를 주세요(size는 선택, 기본 10)")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true),
            @Parameter(name = "page", description = "페이지 번호, 0이상이어야 함, query string"),
            @Parameter(name = "size", description = "(선택적) 페이지당 컨텐츠 개수, 기본 10, query string")})
    public ResponseEntity<?> findByTitle(@RequestParam String title,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size){
        PageRequest pageable = PageRequest.of(page, size);
        Page<ResponseReviewDto> responseReviews = reviewService.searchReviews(pageable, title);

        if (responseReviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("검색 된 후기가 없습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseReviews);
    }

    //리뷰 생성
    @PostMapping
    @Operation(summary = "면접 후기 생성 API", description = "면접 후기를 생성하는 API 입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> createReview(/*@Valid*/ @RequestBody RequestReviewDto requestReviewDto,
                                          @AuthenticationPrincipal User user) {
        ResponseReviewDto createdReview = reviewService.createReview(requestReviewDto, user.getPid());
        return ResponseEntity.status(HttpStatus.OK).body(createdReview);
    }
    @PutMapping("/{review_id}")
    @Operation(summary = "면접 후기 업데이트 API", description = "면접 후기를 수정하는 API 입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> updateReview(@PathVariable Long review_id,
                                          /*@Valid */RequestReviewDto updateDto,
                                          @AuthenticationPrincipal User user){

        log.info("companyName: {} ", updateDto.companyName());
        log.info("process: {} ", updateDto.process());
        log.info("interview: {} ", updateDto.interviewQuestions());
        log.info("evaluation: {}", updateDto.evaluation());
        log.info("result: {}", updateDto.result());

        reviewService.updateReview(review_id, updateDto, user.getPid());
        return ResponseEntity.status(HttpStatus.OK).body("면접 후기를 성공적으로 수정하였습니다.");
    }
    @DeleteMapping("/{review_id}")
    @Operation(summary = "면접 후기 삭제 API", description = "면접 후기를 삭제하는 API 입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> deleteReview(@PathVariable Long review_id,
                                          @AuthenticationPrincipal User user){
        reviewService.deleteReview(review_id, user.getPid());
        return ResponseEntity.status(HttpStatus.OK).body("면접 후기를 성공적으로 삭제하였습니다.");
    }

}
