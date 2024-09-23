package org.v1.job_coach.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.v1.job_coach.dto.user.request.UserUpdateRequest;
import org.v1.job_coach.dto.user.response.UserDeleteResponse;
import org.v1.job_coach.dto.user.response.UserInfoResponse;
import org.v1.job_coach.dto.user.response.UserUpdateResponse;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.service.user.UserService;

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
        UserInfoResponse userInfo = userService.getUserInfo(user);
        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    }

    // 회원 정보 수정
    @Operation(summary = "회원 정보 업데이트", description = "회원정보를 업데이트합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "성공"), @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")})
    @PutMapping("/my-page")
    public ResponseEntity<?> updateUserInfo(@AuthenticationPrincipal User user,
                                            @RequestBody UserUpdateRequest updateRequest) {
        UserUpdateResponse updateResponse = userService.updateUserInfo(user, updateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updateResponse);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 진행합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "성공"), @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")})
    @DeleteMapping("/my-page")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal User user) {
        UserDeleteResponse deleteResponse = userService.deleteUser(user);
        if (!deleteResponse.result()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원탈퇴를 처리할 수 없습니다. 문제가 발생했습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("성공적으로 회원탈퇴 되었습니다. 이용해 주셔서 감사합니다.");
    }











}