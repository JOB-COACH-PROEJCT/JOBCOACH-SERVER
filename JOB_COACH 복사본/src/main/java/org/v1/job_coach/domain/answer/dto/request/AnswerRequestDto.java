package org.v1.job_coach.domain.answer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record AnswerRequestDto(
        @Schema(description = "면접 질문 id", defaultValue = "1")
        Long questionId,
        @Schema(description = "면접 질문 id", defaultValue = "제 강점은 문제 해결 능력과 협업 능력입니다. 복잡한 문제를 체계적으로 분석하고, 최적의 해결책을 찾는 데 자신이 있습니다. 특히, 개발 과정에서 예상치 못한 이슈가 발생했을 때 빠르게 대응하여 해결할 수 있는 능력을 갖추고 있습니다. 또한, 팀 내에서 협업할 때 의견을 명확하게 전달하고, 다른 사람들의 아이디어를 경청하면서 상호 발전할 수 있는 환경을 만드는 데 기여하는 편입니다. 이러한 협업 능력 덕분에 프로젝트를 성공적으로 마무리한 경험이 많습니다.\n" +
                "\n" + "하지만 저의 약점은 가끔 너무 세부적인 부분에 신경을 쓰다 보니, 전체적인 일정 관리에 어려움을 겪을 때가 있다는 점입니다. 세부 사항에 집중하는 것이 장점일 때도 있지만, 때로는 큰 그림을 놓치는 경우가 발생할 수 있습니다. 이를 보완하기 위해 일정 관리 도구를 적극 활용하고, 우선순위를 명확히 설정하는 훈련을 하고 있습니다.")
        String answerContent
) {
}
