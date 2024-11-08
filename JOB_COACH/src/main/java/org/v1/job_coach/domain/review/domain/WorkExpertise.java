package org.v1.job_coach.domain.review.domain;

import lombok.Getter;

@Getter
public enum WorkExpertise {
    ENTRY_LEVEL("신입"),
    JUNIOR("1-3년차"),
    MID_LEVEL("4-6년차"),
    SENIOR("7-9년차"),
    EXPERT("10년차 이상");

    private final String description;

    WorkExpertise(String description) {
        this.description = description;
    }

}
