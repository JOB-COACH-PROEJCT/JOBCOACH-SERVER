/*
package org.v1.job_coach.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.v1.job_coach.security.annotation.AdminAuthorize;


@Tag(name = "관리자용 API")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "회원 목록 조회")
    @GetMapping("/users")
    public ResponseEntity<?> getAllMembers() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getUsers());
    }

    @Operation(summary = "관리자 목록 조회")
    @GetMapping("/admins")
    public ResponseEntity<?> getAllAdmins() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAdmins());
    }
}
*/
