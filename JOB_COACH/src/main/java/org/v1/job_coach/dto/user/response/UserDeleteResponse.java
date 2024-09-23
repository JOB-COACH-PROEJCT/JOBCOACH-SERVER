
package org.v1.job_coach.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.v1.job_coach.entity.User;

@Schema(description = "유저 회원탈퇴 DTO")
public record UserDeleteResponse(
        @Schema(description = "회원 삭제 성공 여부", example = "true")
        boolean result
) {
        public static UserDeleteResponse toDto() {
                return new UserDeleteResponse(true);

        }
}