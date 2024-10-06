package org.v1.job_coach.domain.consulting.application;

import org.springframework.data.domain.Page;
import org.v1.job_coach.domain.answer.domain.Answer;
import org.v1.job_coach.domain.consulting.domain.Consulting;
import org.v1.job_coach.domain.consulting.dto.response.ConsultingResponseDto;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.user.domain.User;

import java.util.Map;

public interface ConsultingService {
    Consulting processConsulting(Long answerId);

    Map<String, Object> callChatGpt(Answer answer);

    Map<String, Object> createConsulting(Answer answer, String model);

    ResultResponseDto<?> getConsultingByChatRoom(Long roomId, int page, User user);

}