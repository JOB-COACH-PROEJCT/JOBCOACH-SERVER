package org.v1.job_coach.global.error;

import lombok.Getter;

@Getter
/* 예외 상황에 대한 상태코드와 에러 메시지 정의 */
public enum Error {
    INTERNAL_SERVER_ERROR("서버에 문제가 발생했습니다.", 500),
    ACCESS_DENIED("접근이 거부되었습니다.", 403),

    INVALID_PAGE("페이지 번호가 범위를 벗어났습니다. 페이지 번호를 확인해 주세요.", 400),
    INVALID_CREDENTIALS("아이디 또는 비밀번호가 일치하지 않습니다.", 401),
    INVALID_PASSWORD("입력한 비밀번호가 잘못되었습니다. 다시 시도해 주세요.", 401),
    USERNAME_ALREADY_TAKEN("이미 사용 중인 아이디입니다.", 409),


    DUPLICATE_USER("이미 가입 된 유저입니다.", 400),
    DEACTIVATED_USER("이미 탈퇴 된 유저입니다.", 400),
    NOT_FOUND_BOARD("게시글을 찾을 수 없습니다.",404),
    NOT_FOUND_REVIEW("면접후기를 찾을 수 없습니다",404),
    NOT_AUTHOR("작성자만 게시글을 삭제할 수 있습니다.", 403),

    NOT_FOUND_MATCH("존재하지 않는 매칭입니다.", 404),
    NOT_FOUND_COACH("존재하지 않는 면접코치입니다.", 404),
    NOT_FOUND_COMMENT("존재하지 않는 댓글입니다.", 404),
    NOT_FOUND_USER("존재하지 않는 회원입니다.", 404),
    NOT_FOUND_CHATROOM("존재하지 않는 채팅방입니다.", 404),
    NOT_FOUND_ANSWER("존재하지 않는 답변입니다.", 404),
    NOT_FOUND_QUESTION("존재하지 않는 질문입니다.", 404),
    NOT_FOUND_CONSULTING("존재하지 않는 컨설팅입니다.", 404),
    NOT_AUTHORIZED("접근할 권한이 없습니다.", 403),

    MATCHING_INVALID_STATUS("매칭 상태가 유효하지 않습니다.", 409),
    MATCHING_ALREADY_APPROVED("이미 승인된 매칭은 취소할 수 없습니다.", 409),
    MATCHING_NOT_REQUESTED("매칭이 요청 상태여야 합니다.", 422),
    MATCHING_NOT_APPROVED("매칭이 승인 상태여야 합니다.", 422),


    INVALID_INPUT("입력이 유효하지 않습니다.", 400),
    ERROR_OPENAI_RESPONSE("AI 응답에 오류가 발생하였습니다.", 500);

    private final String errorMessage;
    private final int state;

    Error(String errorMessage, int state) {
        this.errorMessage = errorMessage;
        this.state = state;
    }
}
