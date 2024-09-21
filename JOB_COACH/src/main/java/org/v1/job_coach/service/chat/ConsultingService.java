package org.v1.job_coach.service.chat;

import org.springframework.stereotype.Service;
import org.v1.job_coach.entity.chat.Answer;
import org.v1.job_coach.entity.chat.Consulting;

import java.util.Map;

public interface ConsultingService {

    Consulting processConsulting(Answer answer) throws Exception;

    Map<String, Object> callChatGpt(Answer answer) throws Exception;

    Map<String, Object> createConsulting(Answer answer, String model);


}
