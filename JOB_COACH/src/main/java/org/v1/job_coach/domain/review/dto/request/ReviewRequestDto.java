package org.v1.job_coach.domain.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;
public record ReviewRequestDto(
        @NotBlank(message = "제목은 필수 항목입니다.")
        @Size(min = 2, max = 20, message = "2자 이상, 20자 이하로 입력해주세요.")
        @Schema(description = "면접 리뷰 제목", defaultValue = "HA 기업 면접 후기")
        String title,

        @NotBlank(message = "기업명은 필수 항목입니다.")
        @Schema(description = "면접 리뷰 회사", defaultValue = "HA 주식회사")
        String companyName,

        @NotNull(message = "평가는 필수 항목입니다.")
        @Schema(description = "면접 난이도", defaultValue = "POSITIVE", allowableValues = {"POSITIVE", "NEUTRAL", "NEGATIVE"})
        String evaluation,

        @NotNull(message = "결과는 필수 항목입니다.")
        @Schema(description = "면접 결과", defaultValue = "PASS", allowableValues = {"PASS", "FAIL"})
        String result,

        @NotBlank(message = "내용을 입력해주세요.")
        @Schema(description = "면접 진행", defaultValue = "서류 전형, 인적성 검사, 1차 면접(실무진 면접), 2차 면접(임원 면접) 순으로 진행되었습니다. 서류 전형 이후 일주일 안에 연락이 왔고, 면접은 비대면으로 진행되었습니다.")
        String process,

        @NotEmpty(message = "면접 질문을 하나 이상 입력해주세요.")
        @Schema(description = "면접 질문", defaultValue = "[\"자신의 가장 큰 장점은 무엇인가요?\", \"최근에 겪었던 어려운 상황과 이를 해결한 방법을 설명해 주세요.\", \"이 직무에서 어떤 성과를 기대할 수 있을까요?\"]")
        List<String> interviewQuestions,

        @NotBlank(message = "내용을 입력해주세요.")
        @Schema(description = "면접 Tip", defaultValue = "면접 준비 시 자기소개서 내용을 철저히 준비하는 것이 중요합니다. 또한, 회사의 최근 이슈에 대해 파악하고 그에 맞는 질문이 나올 수 있으니 대비하는 것이 좋습니다.")
        String tips
) {
}

