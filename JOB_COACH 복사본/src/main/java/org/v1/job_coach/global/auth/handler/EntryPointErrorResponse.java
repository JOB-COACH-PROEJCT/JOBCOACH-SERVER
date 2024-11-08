package org.v1.job_coach.global.auth.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EntryPointErrorResponse {
    private int code;
    private String msg;
    private String path;
    private String timestamp;

    EntryPointErrorResponse(int code, String msg, String path) {
        this.code = code;
        this.msg = msg;
        this.path = path;
    }
}
