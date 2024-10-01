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
    public UserInfoResponseDto getUserInfo(User user) {
        return UserInfoResponseDto.toDto(isUserValid(user));
    }
    

    @Override
    @Transactional
    public UserUpdateResponseDto updateUserInfo(User user, UserUpdateRequestDto updateRequest) {
        User findUser = isUserValid(user);
        /* 비밀번호 일치 확인 */
        if (!passwordEncoder.matches(updateRequest.password(), findUser.getPassword())) {
            throw new CustomException(Error.INVALID_PASSWORD);
        }
        String encode = passwordEncoder.encode(updateRequest.updatePassword());
        boolean isUpdate = findUser.updateUser(updateRequest, encode);



        return UserUpdateResponseDto.toDto(findUser, isUpdate);
    }

    @Override
    @Transactional
    public UserDeleteResponseDto deleteUser(User user) {
        User deletedUser = isUserValid(user);
        deletedUser.deactivate();
        return UserDeleteResponseDto.toDto();
    }

    public User isUserValid(User user) {
        User validationUser = userRepository.findById(user.getPid()).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
        /*
         - 탈퇴 유저인지 확인
         if (user.isDeactivated()) {
            throw new CustomException("탈퇴한 사용자입니다.");
        }*/
        return validationUser;
    }
}
