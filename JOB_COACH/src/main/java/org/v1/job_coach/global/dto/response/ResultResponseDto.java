package org.v1.job_coach.global.dto.response;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

public record ResultResponseDto<T>(
        int state,
        String message,
        T data,
        String timestamp,
        String path
) {
    public static <T> ResultResponseDto<T> toDataResponseDto(int state, String message, T data) {
        return new ResultResponseDto<T>(
                state,
                message,
                data,
                LocalDateTime.now().toString(),
                ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
    }

    public static <T> ResultResponseDto<T> toResultResponseDto(int state, String message) {
        return new ResultResponseDto<T>(
                state,
                message,
                null,
                LocalDateTime.now().toString(),
                ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
    }
}
