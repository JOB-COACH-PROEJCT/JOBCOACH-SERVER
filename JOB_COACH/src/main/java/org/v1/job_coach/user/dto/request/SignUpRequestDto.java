package org.v1.job_coach.user.dto.request;

public record SignUpRequestDto(
        String name,
        String email,
        String number,
        String password,
        String profile
){
}