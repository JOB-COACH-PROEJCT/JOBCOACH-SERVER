package org.v1.job_coach.domain.mypage.application.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.v1.job_coach.domain.mypage.application.UserService;
import org.v1.job_coach.domain.mypage.dto.request.UserUpdateRequestDto;
import org.v1.job_coach.domain.mypage.dto.response.UserDeleteResponseDto;
import org.v1.job_coach.domain.mypage.dto.response.UserInfoResponseDto;
import org.v1.job_coach.domain.mypage.dto.response.UserUpdateResponseDto;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.user.dao.UserRepository;
import org.v1.job_coach.user.domain.User;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public ResultResponseDto<?> getUserInfo(User user) {
        return ResultResponseDto.toDataResponseDto(200, "회원 정보를 성공적으로 불러왔습니다.", UserInfoResponseDto.toDto(isUserValid(user)));
    }
    

    @Override
    @Transactional
    public ResultResponseDto<?> updateUserInfo(User user, UserUpdateRequestDto updateRequest) {
        User findUser = isUserValid(user);
        /* 비밀번호 일치 확인 */
        if (!passwordEncoder.matches(updateRequest.password(), findUser.getPassword())) {
            throw new CustomException(Error.INVALID_PASSWORD);
        }
        String encode = passwordEncoder.encode(updateRequest.updatePassword());
        boolean isUpdate = findUser.updateUser(updateRequest, encode);
        return ResultResponseDto.toDataResponseDto(
                200,
                "회원정보를 성공적으로 업데이트 하였습니다.",
                UserUpdateResponseDto.toDto(findUser, isUpdate));
    }

    @Override
    @Transactional
    public ResultResponseDto<?> deleteUser(User user) {
        User deletedUser = isUserValid(user);

        if(!deletedUser.isActive()){
            return ResultResponseDto.toResultResponseDto(
                    410,
                    "이미 회원 탈퇴된 사용자입니다.");
        }

        deletedUser.deactivate();
        UserDeleteResponseDto.toDto();
        return ResultResponseDto.toResultResponseDto(
                200,
                "회원정보를 성공적으로 업데이트 하였습니다.");
    }

    private User isUserValid(User user) {
        return userRepository.findById(user.getPid()).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
    }
}
