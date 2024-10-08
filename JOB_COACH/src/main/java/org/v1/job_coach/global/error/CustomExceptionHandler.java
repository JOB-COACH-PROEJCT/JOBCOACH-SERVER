package org.v1.job_coach.global.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(HttpServletRequest request, CustomException ex) {
        String path = request.getRequestURI(); //요청 url 가져오기
        ErrorResponse errorDto = ErrorResponse.toErrorDto(ex, path);

        /*ResponseEntity 객체생성 인자값 -> (body, status)*/
        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(ex.getState()));
    }

    // IllegalArgumentException, NoSuchElementException 처리
    /*@ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleCommonException(Exception e) {
        ExceptionResponse exceptionResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.badRequest().body(exceptionResponse);
    }


        return ResponseEntity.badRequest().body(ErrorResponse.error(e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("접근이 거부되었습니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnexpectedException() {
        return ResponseEntity.internalServerError().body(ApiResponse.error("서버에 문제가 발생했습니다."));
    }
*/

}
