package org.v1.job_coach.coach.domain;

public enum MatchingStatus {
    REQUESTED,    /*사용자가 코치에게 매칭을 요청한 상태*/
    APPROVED,     /*코치가 요청을 승인한 상태*/
    REJECTED,     /*요청이 거절된 상태*/
    CANCELLED,    /*매칭이 취소된 상태*/
    COMPLETED     /*면접이 완료된 상태*/
}
