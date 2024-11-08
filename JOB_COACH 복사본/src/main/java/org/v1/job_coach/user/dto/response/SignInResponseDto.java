package org.v1.job_coach.user.dto.response;

import lombok.*;
import org.v1.job_coach.global.util.DateFormatter;

import java.time.LocalDateTime;


@Getter
public class SignInResponseDto{
    private final boolean success;
    private final int code;
    private final String msg;
    private final String timestamp;
    private final String token;

    public SignInResponseDto(int code, String msg, String token) {
        if (code == 200) {
            this.success = true;
            this.code = code;
            this.msg = msg;
            this.timestamp = DateFormatter.getDateNow(LocalDateTime.now());
            this.token = token;
        } else if (code == 409) {
            this.success = false;
            this.code = code;
            this.msg = msg;
            this.timestamp = DateFormatter.getDateNow(LocalDateTime.now());
            this.token = token;
        } else {
            this.success = false;
            this.code = code;
            this.msg = msg;
            this.timestamp = DateFormatter.getDateNow(LocalDateTime.now());
            this.token = token;
        }
    }
}
