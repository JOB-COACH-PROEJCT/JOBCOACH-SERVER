package org.v1.job_coach.user.application;

import org.v1.job_coach.coach.dto.CoachSignUpRequestDto;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.user.dto.request.SignInRequestDto;
import org.v1.job_coach.user.dto.response.SignInResponseDto;
import org.v1.job_coach.user.dto.request.SignUpRequestDto;
import org.v1.job_coach.user.dto.response.SignUpResponseDto;

public interface SignService {
    SignUpResponseDto SignUp(SignUpRequestDto sIgnUpRequestDto);

    SignInResponseDto SignIn(SignInRequestDto sign);

    ResultResponseDto<?> SignUpCoach(Long userId, CoachSignUpRequestDto coachSignUpRequestDto);
}
