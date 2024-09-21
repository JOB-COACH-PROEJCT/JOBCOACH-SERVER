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
        if(roles.equalsIgnoreCase("admin")){
            user = User.builder()
                    .email(signUpDto.getEmail())
                    .number(signUpDto.getNumber())
                    .password(passwordEncoder.encode(signUpDto.getPassword()))
                    .name(signUpDto.getName())
                    .roles(Collections.singletonList("ROLE_ADMIN"))
                    .build();
        }else{
            user = User.builder()
                    .email(signUpDto.getEmail())
                    .number(signUpDto.getNumber())
                    .password(passwordEncoder.encode(signUpDto.getPassword()))
                    .name(signUpDto.getName())
                    .roles(Collections.singletonList("ROLE_babyLion"))
                    .build();
        }

        User savedUser = userRepository.save(user);

        SignUpResultDto signUpResultDto = new SignUpResultDto();
        logger.info("[getSignResultDto] babyLion 정보 들어왔는지 확인 후 결과값 주입");

        if(!savedUser.getEmail().isEmpty()){
            setSucces(signUpResultDto);
        }else{
            setFail(signUpResultDto);
        }

        return signUpResultDto;

    }

    @Override
    public SignInResultDto SignIn(String email, String password)throws RuntimeException{
        User user = userRepository.getByEmail(email);
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
        setSucces(signInResultDto);
        return signInResultDto;
    }

    private void setSucces(SignUpResultDto result){
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