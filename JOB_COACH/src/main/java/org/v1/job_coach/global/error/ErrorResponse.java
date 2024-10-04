package org.v1.job_coach.global.error;

import java.time.LocalDateTime;

public record ErrorResponse(
        int state,
        String errorMessage,
        String errorCode, //Error Enum의 이름을 사용하여 에러 코드 제공
        String timestamp,
        String path
) {
    public static ErrorResponse toErrorDto(CustomException ex, String path) {
        return new ErrorResponse(
                ex.getState(),
                ex.getMessage(),
                ex.getError().name(),
                LocalDateTime.now().toString(),
                path
        );
    }
}
