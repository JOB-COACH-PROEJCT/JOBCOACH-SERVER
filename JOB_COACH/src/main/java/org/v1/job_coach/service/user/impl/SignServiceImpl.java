package org.v1.job_coach.service.user.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.v1.job_coach.dto.CommonResponse;
import org.v1.job_coach.dto.SignDto.SignInResultDto;
import org.v1.job_coach.dto.SignDto.SignUpDto;
import org.v1.job_coach.dto.SignDto.SignUpResultDto;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.exception.CustomException;
import org.v1.job_coach.exception.Error;
import org.v1.job_coach.repository.UserRepository;
import org.v1.job_coach.security.jwt.JwtTokenProvider;
import org.v1.job_coach.service.user.SignService;

import java.util.Collections;


@Service
public class SignServiceImpl implements SignService {

    private final Logger logger = LoggerFactory.getLogger(SignServiceImpl.class);

    private JwtTokenProvider jwtProvider;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public SignServiceImpl(JwtTokenProvider jwtProvider, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SignUpResultDto SignUp(SignUpDto signUpDto, String roles){
        User user;
        // 활성화된 이메일 중복 체크
        User existingUser = userRepository.getByEmail(signUpDto.email());
        if (existingUser != null) {
            if (!existingUser.isActive()) {
                String encode = passwordEncoder.encode(signUpDto.password());
                existingUser.activate(signUpDto, encode);
                // 비활성 사용자 재활성화 및 비밀번호 업데이트
                userRepository.save(existingUser); // 기존 유저 업데이트 저장

                // 성공 응답 생성
                SignUpResultDto resultDto = new SignUpResultDto();
                setSuccess(resultDto);
                return resultDto;
            } else {
                // 이미 활성화된 사용자 존재 (중복 가입 방지)
                throw new CustomException(Error.DUPLICATE_USER);
            }
        }else {
            if (roles.equalsIgnoreCase("admin")) {
                user = User.builder()
                        .isActive(true)
                        .email(signUpDto.email())
                        .number(signUpDto.number())
                        .password(passwordEncoder.encode(signUpDto.password()))
                        .name(signUpDto.name())
                        .profile(signUpDto.password())
                        .roles(Collections.singletonList("ROLE_ADMIN"))
                        .build();
            } else {
                user = User.builder()
                        .isActive(true)
                        .email(signUpDto.email())
                        .number(signUpDto.number())
                        .password(passwordEncoder.encode(signUpDto.password()))
                        .name(signUpDto.name())
                        .profile(signUpDto.password())
                        .roles(Collections.singletonList("ROLE_babyLion"))
                        .build();
            }

            User savedUser = userRepository.save(user);

            SignUpResultDto signUpResultDto = new SignUpResultDto();
            logger.info("[getSignResultDto] babyLion 정보 들어왔는지 확인 후 결과값 주입");

            if (!savedUser.getEmail().isEmpty()) {
                setSuccess(signUpResultDto);
            } else {
                setFail(signUpResultDto);
            }
            return signUpResultDto;
        }
    }

    @Override
    public SignInResultDto SignIn(String email, String password)throws RuntimeException{
        User user = userRepository.getByEmail(email);
        // 사용자 존재 여부 및 활성 상태 확인
        if (user == null || !user.isActive()) {
            throw new CustomException(Error.NOT_FOUND_USER);
        }

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException();
        }
        logger.info("[getSignInResult] 패스워드 일치");

        logger.info("[getSignInResult] SignInResultDto 객체 생성");
        SignInResultDto signInResultDto = SignInResultDto.builder()
                .token(jwtProvider.createToken(String.valueOf(user.getEmail()),
                        user.getRoles()))
                .build();
        logger.info("[getSignInResult] SignInResultDto 객체에 값 주입");
        setSuccess(signInResultDto);
        return signInResultDto;
    }

    private void setSuccess(SignUpResultDto result){
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFail(SignUpResultDto result){
        result.setSuccess(true);
        result.setCode(CommonResponse.Fail.getCode());
        result.setMsg(CommonResponse.Fail.getMsg());
    }

}