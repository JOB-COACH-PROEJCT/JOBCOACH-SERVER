package org.v1.job_coach.user.dto;

public record UserDto(
    String email,
    String fullName,
    String phoneNumber
) {
    public UserDto(String email, String fullName, String phoneNumber) {
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }
}
