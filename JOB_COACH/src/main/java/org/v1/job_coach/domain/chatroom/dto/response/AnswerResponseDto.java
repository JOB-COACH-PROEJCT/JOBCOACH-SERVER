package org.v1.job_coach.domain.chatroom.dto.response;

import org.v1.job_coach.domain.answer.domain.Answer;

public record AnswerResponseDto(
        Long id,
        String answerContent,
        Long chatRoomId,
        Long questionId,
        String userName
) {
    public static AnswerResponseDto toAnswerResponseDto(Answer answer) {
        return new AnswerResponseDto(
                answer.getId(),
                answer.getContent(),
                answer.getChatRoom().getId(),
                answer.getQuestion().getId(),
                answer.getUser().getName()
        );
    }
}
