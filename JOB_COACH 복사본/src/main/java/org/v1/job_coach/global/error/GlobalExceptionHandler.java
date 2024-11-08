package org.v1.job_coach.global.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    /*유효성 검사 실패 시 발생하는 MethodArgumentNotValidException 예외를 처리하는 메서드*/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // HTTP 응답 상태 코드를 400 BAD REQUEST로 설정
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                    HttpServletRequest request) {
        CustomException customEx = new CustomException(Error.INVALID_INPUT);
        String path = request.getRequestURI();

        String errorMessage = ex.getBindingResult()  // 유효성 검사 실패로 발생한 필드 오류들을 가져와서
                .getFieldErrors()   // 필드 오류 목록을 가져옴
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()) // 필드 이름과 오류 메시지를 조합
                .collect(Collectors.joining(", ")); // 오류 메시지를 쉼표로 구분하여 하나의 문자열로 결합

        ErrorResponse errorResponse = new ErrorResponse(
                customEx.getState(),
                errorMessage,
                customEx.getError().name(),
                LocalDateTime.now().toString(),
                path
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
