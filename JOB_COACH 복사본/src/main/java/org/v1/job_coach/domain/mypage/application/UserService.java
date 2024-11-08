package org.v1.job_coach.domain.mypage.application;

import org.v1.job_coach.domain.mypage.dto.request.UserUpdateRequestDto;
import org.v1.job_coach.domain.mypage.dto.response.UserDeleteResponseDto;
import org.v1.job_coach.domain.mypage.dto.response.UserInfoResponseDto;
import org.v1.job_coach.domain.mypage.dto.response.UserUpdateResponseDto;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.user.domain.User;

public interface UserService {

    ResultResponseDto<?> getUserInfo(User user);

    ResultResponseDto<?> updateUserInfo(User user, UserUpdateRequestDto updateRequest);

    ResultResponseDto<?> deleteUser(User user);

}
