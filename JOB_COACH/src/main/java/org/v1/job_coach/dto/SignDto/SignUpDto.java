package org.v1.job_coach.dto.SignDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public record SignUpDto (
        String name,
        String email,
        String number,
        String password,
        String profile
){
}