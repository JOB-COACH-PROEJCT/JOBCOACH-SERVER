package org.v1.job_coach.dto.SignDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SignUpDto {
    private String email;
    private String number;
    private String password;
    private String name;
}