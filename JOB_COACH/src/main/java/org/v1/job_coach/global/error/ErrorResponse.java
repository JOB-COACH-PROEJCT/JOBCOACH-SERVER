package org.v1.job_coach.global.error;

public record ErrorResponse(
        Error error,
        int state,
        String errorMessage
) {
    public static ErrorResponse error(CustomException ex) {
        return new ErrorResponse(ex.getError(), ex.getState(), ex.getMessage());
    }
}
