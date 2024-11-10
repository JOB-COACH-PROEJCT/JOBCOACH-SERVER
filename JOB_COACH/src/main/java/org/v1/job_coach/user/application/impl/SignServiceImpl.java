package org.v1.job_coach.user.application.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v1.job_coach.coach.dao.CoachRepository;
import org.v1.job_coach.coach.domain.Coach;
import org.v1.job_coach.coach.dto.CoachSignUpRequestDto;
import org.v1.job_coach.global.auth.jwt.JwtTokenProvider;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.user.dto.CommonResponse;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.user.application.SignService;
import org.v1.job_coach.user.dao.UserRepository;
import org.v1.job_coach.user.domain.User;
import org.v1.job_coach.user.dto.request.SignInRequestDto;
import org.v1.job_coach.user.dto.request.SignUpRequestDto;
import org.v1.job_coach.user.dto.response.SignInResponseDto;
import org.v1.job_coach.user.dto.response.SignUpResponseDto;

import java.util.Collection;
import java.util.Collections;


@Service
public class SignServiceImpl implements SignService {

    private final Logger logger = LoggerFactory.getLogger(SignServiceImpl.class);

    private JwtTokenProvider jwtProvider;
    private UserRepository userRepository;
    private CoachRepository coachRepository;
    private PasswordEncoder passwordEncoder;

    public SignServiceImpl(JwtTokenProvider jwtProvider, UserRepository userRepository, CoachRepository coachRepository, PasswordEncoder passwordEncoder) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.coachRepository = coachRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SignUpResponseDto SignUp(SignUpRequestDto signUpRequestDto){
        User user;
        // 활성화된 이메일 중복 체크
        User existingUser = userRepository.getByEmail(signUpRequestDto.email());
        if (existingUser != null) {
            if (!existingUser.isActive()) {
                String encode = passwordEncoder.encode(signUpRequestDto.password());
                existingUser.activate(signUpRequestDto, encode);
                // 비활성 사용자 재활성화 및 비밀번호 업데이트
                userRepository.save(existingUser); // 기존 유저 업데이트 저장
                return new SignUpResponseDto(CommonResponse.SIGNUP_SUCCESS.getCode(), CommonResponse.SIGNUP_SUCCESS.getMsg(), existingUser.getPid());
            } else {
                // 이미 활성화된 사용자 존재 (중복 가입 방지)
                throw new CustomException(Error.DUPLICATE_USER);
            }
        }else {
            if (signUpRequestDto.roles().equalsIgnoreCase("admin")) {
                user = User.builder()
                        .isActive(true)
                        .email(signUpRequestDto.email())
                        .number(signUpRequestDto.number())
                        .password(passwordEncoder.encode(signUpRequestDto.password()))
                        .name(signUpRequestDto.name())
                        .profile(signUpRequestDto.password())
                        .roles(Collections.singletonList("ROLE_ADMIN"))
                        .build();
            } else if (signUpRequestDto.roles().equalsIgnoreCase("coach")) {
                user = new Coach(
                        true,
                        signUpRequestDto.email(),
                        signUpRequestDto.number(),
                        passwordEncoder.encode(signUpRequestDto.password()),
                        signUpRequestDto.name(),
                        signUpRequestDto.profile(),
                        Collections.singletonList("ROLE_COACH")
                );
            }else {
                user = User.builder()
                        .isActive(true)
                        .email(signUpRequestDto.email())
                        .number(signUpRequestDto.number())
                        .password(passwordEncoder.encode(signUpRequestDto.password()))
                        .name(signUpRequestDto.name())
                        .profile(signUpRequestDto.password())
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build();
            }
            User savedUser = userRepository.save(user);
            logger.info("[getSignResultDto] User 정보 들어왔는지 확인 후 결과값 주입");

            if (!savedUser.getEmail().isEmpty()) {
                return new SignUpResponseDto(CommonResponse.SIGNUP_SUCCESS.getCode(), CommonResponse.SIGNUP_SUCCESS.getMsg(), savedUser.getPid());
            } else {
                return new SignUpResponseDto(CommonResponse.SIGNUP_FAIL.getCode(), CommonResponse.SIGNUP_SUCCESS.getMsg(), null);
            }

        }
    }

    @Override
    public SignInResponseDto SignIn(SignInRequestDto sign)/* throws RuntimeErrorException */{
        User user = userRepository.getByEmail(sign.email());

        /* 사용자 존재 여부 및 활성 상태 확인 */
        if (user == null || !user.isActive()) {
            throw new CustomException(Error.NOT_FOUND_USER);
        }

        /* 비밀번호가 일치하는지 확인 */
        if(!passwordEncoder.matches(sign.password(), user.getPassword())){
            throw new CustomException(Error.INVALID_PASSWORD);
        }
        logger.info("[getSignInResult] 패스워드 일치");
        logger.info("[getSignInResult] token 생성");
        String token = jwtProvider.createToken(user.getEmail(), user.getUsername(), user.getRoles());

        logger.info("[getSignInResult] SignInResultDto 객체에 값 주입");
        return new SignInResponseDto(CommonResponse.LOGIN_SUCCESS.getCode(), CommonResponse.LOGIN_SUCCESS.getMsg(), token);
    }

    @Override
    @Transactional
    public ResultResponseDto<?> SignUpCoach(Long userId, CoachSignUpRequestDto coachSignUpRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        if (!user.getRoles().contains("ROLE_COACH")) {
            throw new CustomException(Error.NOT_AUTHORIZED);
        }

        Coach coach = (Coach) user;
        coach.updateCoachDetails(coachSignUpRequestDto);

        return ResultResponseDto.toResultResponseDto(201, "Coach 회원가입을 완료하였습니다.");
    }
}