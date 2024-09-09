package org.v1.job_coach.exception;

import lombok.Getter;
@Getter
public class CustomException extends RuntimeException {

    private final Error error;
    private final int state;

    public CustomException(Error error) {
        super(error.getErrorMessage());
        this.error = error;
        this.state = error.getState();
    }
}
