package org.v1.job_coach.user.dto.response;

import lombok.*;


@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInResultDto extends SignUpResultDto {

    private String token;

    @Builder
    public SignInResultDto(boolean success, int code, String msg, String token) {
        super(success, code, msg);
        this.token = token;
    }
}