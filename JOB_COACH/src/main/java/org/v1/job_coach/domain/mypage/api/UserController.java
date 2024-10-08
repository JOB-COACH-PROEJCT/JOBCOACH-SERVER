package org.v1.job_coach.domain.mypage.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.v1.job_coach.domain.mypage.dto.request.UserUpdateRequestDto;
import org.v1.job_coach.domain.mypage.dto.response.UserDeleteResponseDto;
import org.v1.job_coach.domain.mypage.dto.response.UserInfoResponseDto;
import org.v1.job_coach.domain.mypage.dto.response.UserUpdateResponseDto;
import org.v1.job_coach.domain.mypage.application.UserService;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.user.domain.User;

@Tag(name = "MyPage", description = "마이페이지 API")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "회원 정보 조회", description = "회원정보를 조회합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "성공"), @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")})
    @GetMapping("/my-page")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal User user) {
        ResultResponseDto<?> userResponseDto = userService.getUserInfo(user);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    // 회원 정보 수정
    @Operation(summary = "회원 정보 업데이트", description = "회원정보를 업데이트합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "성공"), @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")})
    @PutMapping("/my-page")
    public ResponseEntity<?> updateUserInfo(@AuthenticationPrincipal User user,
                                            @RequestBody UserUpdateRequestDto updateRequest) {
        ResultResponseDto<?> userResponseDto = userService.updateUserInfo(user, updateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 진행합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "성공"), @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")})
    @DeleteMapping("/my-page")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal User user) {
        ResultResponseDto<?> userResponseDto = userService.deleteUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }











}