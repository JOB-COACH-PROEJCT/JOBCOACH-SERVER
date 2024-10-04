package org.v1.job_coach.user.application;

import org.v1.job_coach.user.dto.request.SignInRequestDto;
import org.v1.job_coach.user.dto.response.SignInResultDto;
import org.v1.job_coach.user.dto.request.SignUpRequestDto;
import org.v1.job_coach.user.dto.response.SignUpResultDto;

public interface SignService {
    SignUpResultDto SignUp(SignUpRequestDto sIgnUpRequestDto);

    SignInResultDto SignIn(SignInRequestDto sign);
}
