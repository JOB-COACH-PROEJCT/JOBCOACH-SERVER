package org.v1.job_coach.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SignUpResponseDto{
        private final boolean success;
        private final int code;
        private final String msg;
        private final String timestamp;

    public SignUpResponseDto(int code, String msg) {
        if (code == 200) {
            this.success = true;
            this.code = code;
            this.msg = msg;
            this.timestamp = LocalDateTime.now().toString();
        } else if (code == 409) {
            this.success = false;
            this.code = code;
            this.msg = msg;
            this.timestamp = LocalDateTime.now().toString();
        } else {
            this.success = false;
            this.code = code;
            this.msg = msg;
            this.timestamp = LocalDateTime.now().toString();
        }
    }
}
