package org.v1.job_coach.service.user.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.v1.job_coach.dto.user.request.UserUpdateRequest;
import org.v1.job_coach.dto.user.response.UserDeleteResponse;
import org.v1.job_coach.dto.user.response.UserInfoResponse;
import org.v1.job_coach.dto.user.response.UserUpdateResponse;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.entity.chat.Answer;
import org.v1.job_coach.entity.chat.ChatRoom;
import org.v1.job_coach.exception.CustomException;
import org.v1.job_coach.exception.Error;
import org.v1.job_coach.repository.UserRepository;
import org.v1.job_coach.repository.chat.AnswerRepository;
import org.v1.job_coach.repository.chat.ChatRoomRepository;
import org.v1.job_coach.service.user.UserService;

import java.util.List;
import java.util.Optional;

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
    public UserInfoResponse getUserInfo(User user) {
        return UserInfoResponse.toDto(isUserValid(user));
    }
    

    @Override
    @Transactional
    public UserUpdateResponse updateUserInfo(User user, UserUpdateRequest updateRequest) {
        User findUser = isUserValid(user);
        /* 비밀번호 일치 확인 */
        if (!passwordEncoder.matches(updateRequest.password(), findUser.getPassword())) {
            throw new CustomException(Error.INVALID_PASSWORD);
        }
        String encode = passwordEncoder.encode(updateRequest.updatePassword());
        boolean isUpdate = findUser.updateUser(updateRequest, encode);



        return UserUpdateResponse.toDto(findUser, isUpdate);
    }

    @Override
    @Transactional
    public UserDeleteResponse deleteUser(User user) {
        User deletedUser = isUserValid(user);
        deletedUser.deactivate();
        return UserDeleteResponse.toDto();
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
