package org.v1.job_coach.coach.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.v1.job_coach.coach.application.impl.CoachServiceImpl;
import org.v1.job_coach.coach.dto.CoachDetailResponseDto;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.user.dto.request.SignInRequestDto;
import org.v1.job_coach.user.dto.response.SignInResponseDto;

@Slf4j
@Tag(name = "Coach", description = "면접 코치 매칭 서비스 API")
@RestController
@RequestMapping("/api/v1/interview")
public class CoachController {

    private final CoachServiceImpl coachService;

    public CoachController(CoachServiceImpl coachService) {
        this.coachService = coachService;
    }

    @GetMapping()
    @Operation(summary = "모든 면접 코치 반환", description = "모든 게시글을 반환하는 API이며, 페이징을 포함합니다." + "query string으로 page 번호를 주세요")
    public ResponseEntity<?> getAllCoaches(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        ResultResponseDto<?> allCoaches = coachService.getAllCoaches(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(allCoaches);
    }

    @Operation(summary = "코치 상세 정보 조회", description = "userId를 통해 면접 코치의 상세 정보를 조회합니다.")
    @GetMapping("/details")
    public ResponseEntity<?> getCoachDetails(@RequestParam Long userId) {
        log.info("[getCoachDetails] userId: {}로 면접 코치의 상세 정보를 조회합니다.", userId);
        ResultResponseDto<?> coachDetailResponse = coachService.getCoachDetails(userId);
        return ResponseEntity.status(HttpStatus.OK).body(coachDetailResponse);
    }
}
