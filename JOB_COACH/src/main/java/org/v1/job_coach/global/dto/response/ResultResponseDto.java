package org.v1.job_coach.global.dto.response;

import java.time.LocalDateTime;

public record ResultResponseDto<T>(
        int state,
        String message,
        T data,
        String timestamp,
        String path
) {
    public static <T> ResultResponseDto<T> toResultResponseDto(int state, String message, T data, String path) {
        return new ResultResponseDto<T>(
                state,
                message,
                data,
                LocalDateTime.now().toString(),
                path
        );
    }
}
