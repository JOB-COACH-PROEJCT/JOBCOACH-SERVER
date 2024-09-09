package org.v1.job_coach.exception;

import lombok.Getter;

@Getter
/* 예외 상황에 대한 상태코드와 에러 메시지 정의 */
public enum Error {
    INTERNAL_SERVER_ERROR("서버에 문제가 발생했습니다.", 500),
    ACCESS_DENIED("접근이 거부되었습니다.", 403),


    NOT_FOUND_USER("존재하지 않는 회원입니다.", 404),
    INVALID_CREDENTIALS("아이디 또는 비밀번호가 일치하지 않습니다.", 400),
    USERNAME_ALREADY_TAKEN("이미 사용 중인 아이디입니다.", 409),

    BOARD_NOT_FOUND("게시글을 찾을 수 없습니다.",404),
    NOT_AUTHOR("작성자만 게시글을 삭제할 수 있습니다.", 403);



    private final int state;
    private final String errorMessage;

    Error(String errorMessage, int state) {
        this.errorMessage = errorMessage;
        this.state = state;
    }
}
