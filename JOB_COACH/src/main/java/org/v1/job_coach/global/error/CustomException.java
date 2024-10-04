package org.v1.job_coach.global.error;

import lombok.Getter;
@Getter
public class CustomException extends RuntimeException {
    private final Error error;

    public CustomException(Error error) {
        super(error.getErrorMessage());
        this.error = error;
    }
    int getState() {
        return error.getState();
    }
    public String getMessage() {
        return error.getErrorMessage();
    }
}


