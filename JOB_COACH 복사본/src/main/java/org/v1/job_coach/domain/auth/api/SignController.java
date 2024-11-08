package org.v1.job_coach.domain.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.v1.job_coach.coach.dto.CoachSignUpRequestDto;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.user.application.SignService;
import org.v1.job_coach.user.dto.request.SignInRequestDto;
import org.v1.job_coach.user.dto.request.SignUpRequestDto;
import org.v1.job_coach.user.dto.response.SignInResponseDto;
import org.v1.job_coach.user.dto.response.SignUpResponseDto;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Sign", description = "로그인 및 회원가입 API")
@RestController
@RequestMapping("/api/v1/sign")
public class SignController {

    private Logger logger = LoggerFactory.getLogger(SignController.class);

    private final SignService signService;

    @Autowired
    public SignController(SignService signService){
        this.signService = signService;
    }

    @Operation(summary = "회원 가입", description = "회원 가입을 진행합니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<?> SignUp(@RequestBody SignUpRequestDto signUpRequestDto){
        logger.info("[signUp] 회원가입을 수행합니다. id : {}, password : ****, name : {}, role : {}",
                signUpRequestDto.email(),
                signUpRequestDto.password(), signUpRequestDto.roles());
        SignUpResponseDto signUpResponseDto = signService.SignUp(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(signUpResponseDto);
    }

    @Operation(summary = "Coach 회원 가입 추가 정보", description = "Coach 회원 가입을 진행합니다.")
    @PostMapping("/sign-up/coach-details")
    public ResponseEntity<?> SignUpCoach(Long userId,
                                         @RequestBody CoachSignUpRequestDto coachSignUpRequestDto){

        ResultResponseDto coachDto = signService.SignUpCoach(userId, coachSignUpRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(coachDto);
    }


    @PostMapping("/login")
    @Operation(summary = "회원 로그인", description = "로그인을 진행합니다.")
    public SignInResponseDto SignIn(@RequestBody SignInRequestDto signInRequestDto) {
        logger.info("[sign-in] 로그인을 시도하고 있습니다. id : {}, password : *****", signInRequestDto.email());
        SignInResponseDto signInResultDto = signService.SignIn(signInRequestDto);
        if(signInResultDto.getCode() == 200){
            logger.info("[sign-in] 정상적으로 로그인이 되었습니다. id: {}, token : {}",signInRequestDto.email(),signInResultDto.getToken());
        }
        return signInResultDto;
    }

    @Operation(summary = "회원 예외처리", description = "접근권한이 없을 시 예외처리를 진행합니다.")
    @GetMapping(value = "/exception")
    public void exceptionTest()throws  RuntimeException{
        throw new CustomException(Error.ACCESS_DENIED);
    }

    //@ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> ExceptionHandler(RuntimeException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        //responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        logger.error("ExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "에러 발생");

        return new ResponseEntity<>(map, responseHeaders, httpStatus);
    }
}