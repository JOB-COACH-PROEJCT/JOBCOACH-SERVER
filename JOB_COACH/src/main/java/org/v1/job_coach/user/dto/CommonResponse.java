package org.v1.job_coach.user.dto;

import lombok.Getter;

@Getter
public enum CommonResponse {
    // 회원가입 성공 및 실패
    SIGNUP_SUCCESS(200, "회원가입이 성공적으로 완료되었습니다."),
    SIGNUP_FAIL(400, "회원가입에 실패했습니다. 다시 시도해 주세요."),

    // 로그인 성공 및 실패
    LOGIN_SUCCESS(200, "로그인이 성공적으로 완료되었습니다."),
    LOGIN_FAIL(401, "로그인에 실패했습니다. 아이디나 비밀번호를 확인해 주세요.");

    int code;
    String msg;

    CommonResponse(int code, String msg){
        this.msg = msg;
        this.code = code;
    }

}
