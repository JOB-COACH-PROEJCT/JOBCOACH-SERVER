package org.v1.job_coach.coach.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.v1.job_coach.coach.application.MatchingService;
import org.v1.job_coach.coach.application.impl.CoachServiceImpl;
import org.v1.job_coach.coach.dto.CoachDetailResponseDto;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.user.domain.User;
import org.v1.job_coach.user.dto.request.SignInRequestDto;
import org.v1.job_coach.user.dto.response.SignInResponseDto;

@Slf4j
@Tag(name = "Coach", description = "면접 코치 매칭 서비스 API")
@RestController
@RequestMapping("/api/v1/coachs")
public class CoachController {

    private final MatchingService matchingService;
    private final CoachServiceImpl coachService;

    public CoachController(MatchingService matchingService, CoachServiceImpl coachService) {
        this.matchingService = matchingService;
        this.coachService = coachService;
    }

    @GetMapping()
    @Operation(summary = "모든 면접 코치 반환", description = "모든 면접 코치를 반환하는 API이며, 페이징을 포함합니다." + "query string으로 page 번호를 주세요")
    public ResponseEntity<?> getAllCoaches(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        ResultResponseDto<?> allCoaches = coachService.getAllCoaches(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(allCoaches);
    }

    @Operation(summary = "코치 상세 정보 조회", description = "CoachID를 통해 면접 코치의 상세 정보를 조회합니다.")
    @GetMapping("/{coach_id}")
    public ResponseEntity<?> getCoachDetails(@PathVariable Long coach_id) {
        log.info("[getCoachDetails] userId: {}로 면접 코치의 상세 정보를 조회합니다.", coach_id);
        ResultResponseDto<?> coachDetailResponse = coachService.getCoachDetails(coach_id);
        return ResponseEntity.status(HttpStatus.OK).body(coachDetailResponse);
    }

    @Operation(summary = "카테고리에 따른 코치 목록 조회", description = "카테고리를 쿼리 파라미터로 전달받아 해당 카테고리에 맞는 면접 코치 목록을 반환합니다.")
    @GetMapping("/category")
    public ResponseEntity<?> getCoachesByCategory(@RequestParam(defaultValue = "") String category,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        log.info("[getCoachesByCategory] 카테고리: {}로 면접 코치 목록을 조회합니다. 페이지: {}, 사이즈: {}", category, page, size);
        ResultResponseDto<?> coachesByCategory = coachService.getCoachesByCategory(category, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(coachesByCategory);
    }

    @Operation(summary = "코치 매칭 요청", description = "면접 코치 1:1 매칭을 신청합니다.")
    @PostMapping("/{coach_id}/match")
    public ResponseEntity<?> Matching(@AuthenticationPrincipal User user, @PathVariable Long coach_id) {
        log.info("[getCoachDetails] userId: {} 매칭을 요청합니다.", user.getPid());
        log.info("[getCoachDetails] coachId: {} 매칭을 요청을 받았습니다.", coach_id);

        ResultResponseDto<?> matchResponse = matchingService.match(user, coach_id);
        return ResponseEntity.status(HttpStatus.OK).body(matchResponse);
    }

    @Operation(summary = "코치 매칭 수락", description = "매칭된 면접 코치 요청을 수락합니다.")
    @PutMapping("/match/{matching_id}/accept")
    public ResponseEntity<?> acceptMatch(@AuthenticationPrincipal User user, @PathVariable Long matching_id) {
        log.info("[acceptMatch] userId: {}가 매칭을 수락합니다.", user.getPid());
        ResultResponseDto<?> acceptResponse = matchingService.acceptMatch(user, matching_id);
        return ResponseEntity.status(HttpStatus.OK).body(acceptResponse);
    }

    @Operation(summary = "코치 매칭 거절", description = "매칭된 면접 코치 요청을 거절합니다.")
    @PutMapping("/match/{matching_id}/reject")
    public ResponseEntity<?> rejectMatch(@AuthenticationPrincipal User user, @PathVariable Long matching_id) {
        log.info("[rejectMatch] userId: {}가 매칭을 거절합니다.", user.getPid());
        ResultResponseDto<?> rejectResponse = matchingService.rejectMatch(user, matching_id);
        return ResponseEntity.status(HttpStatus.OK).body(rejectResponse);
    }

    @Operation(summary = "매칭 취소", description = "매칭된 면접 코치 요청을 취소합니다.")
    @PutMapping("/match/{matching_id}/cancel")
    public ResponseEntity<?> cancelMatch(@AuthenticationPrincipal User user, @PathVariable Long matching_id) {
        log.info("[cancelMatch] userId: {}가 매칭을 취소합니다.", user.getPid());
        ResultResponseDto<?> cancelResponse = matchingService.cancelMatch(user, matching_id);
        return ResponseEntity.status(HttpStatus.OK).body(cancelResponse);
    }

    @Operation(summary = "매칭 완료", description = "매칭을 완료 처리합니다.")
    @PutMapping("/match/{matching_id}/complete")
    public ResponseEntity<?> completeMatch(@AuthenticationPrincipal User user, @PathVariable Long matching_id) {
        log.info("[completeMatch] userId: {}가 매칭을 완료 처리합니다.", user.getPid());
        ResultResponseDto<?> completeResponse = matchingService.completeMatch(user, matching_id);
        return ResponseEntity.status(HttpStatus.OK).body(completeResponse);
    }

    @Operation(summary = "사용자가 매칭한 코치 리스트 조회", description = "사용자가 현재 매칭한 코치 리스트를 조회합니다.")
    @GetMapping("/user/matchings")
    public ResponseEntity<?> getUserMatchings(@AuthenticationPrincipal User user, @RequestParam(defaultValue = "0") int page) {
        log.info("[getUserMatchings] userId: {}가 매칭된 코치 리스트를 조회합니다. 페이지: {}", user.getPid(), page);
        ResultResponseDto<?> userMatchings = matchingService.userMatchingList(user, page);
        return ResponseEntity.status(HttpStatus.OK).body(userMatchings);
    }
    
    @Operation(summary = "코치가 매칭된 사용자 리스트 조회", description = "코치가 현재 매칭된 사용자 리스트를 조회합니다.")
    @GetMapping("/coach/matchings")
    public ResponseEntity<?> getCoachMatchings(@AuthenticationPrincipal User user, @RequestParam(defaultValue = "0") int page) {
        log.info("[getCoachMatchings] coachId: {}가 매칭된 사용자 리스트를 조회합니다. 페이지: {}", user.getPid(), page);
        ResultResponseDto<?> coachMatchings = matchingService.coachMatchingList(user, page);
        return ResponseEntity.status(HttpStatus.OK).body(coachMatchings);
    }


}
