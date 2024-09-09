package org.v1.job_coach.service;

import org.v1.job_coach.dto.SignDto.SignInResultDto;
import org.v1.job_coach.dto.SignDto.SignUpDto;
import org.v1.job_coach.dto.SignDto.SignUpResultDto;

public interface SignService {
    SignUpResultDto SignUp(SignUpDto sIgnUpDto, String roles);
    SignInResultDto SignIn(String email, String password);
}
