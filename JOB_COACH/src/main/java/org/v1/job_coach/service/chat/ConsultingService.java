package org.v1.job_coach.service.chat;

import org.springframework.data.domain.Page;
import org.v1.job_coach.dto.consulting.ConsultingResponseDto;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.entity.chat.Answer;
import org.v1.job_coach.entity.consulting.Consulting;

import java.util.Map;

public interface ConsultingService {
    Consulting processConsulting(Answer answer) throws Exception;

    Map<String, Object> callChatGpt(Answer answer) throws Exception;

    Map<String, Object> createConsulting(Answer answer, String model);

    Page<ConsultingResponseDto> getConsultingByChatRoom(Long roomId, int page, User user);

}