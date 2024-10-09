package org.v1.job_coach.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.v1.job_coach.global.util.DateFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SignUpResponseDto{
        private final boolean success;
        private final Long userId;
        private final int code;
        private final String msg;
        private final String timestamp;

    public SignUpResponseDto(int code, String msg, Long userId) {
        if (code == 200) {
            this.success = true;
            this.userId = userId;
            this.code = code;
            this.msg = msg;
            this.timestamp = DateFormatter.getDateNow(LocalDateTime.now());
        } else if (code == 409) {
            this.success = false;
            this.userId = userId;
            this.code = code;
            this.msg = msg;
            this.timestamp = DateFormatter.getDateNow(LocalDateTime.now());
        } else {
            this.success = false;
            this.userId = null;
            this.code = code;
            this.msg = msg;
            this.timestamp = DateFormatter.getDateNow(LocalDateTime.now());
        }
    }
}
