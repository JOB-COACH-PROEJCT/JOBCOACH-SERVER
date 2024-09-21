package org.v1.job_coach.service.chat;

import org.springframework.http.ResponseEntity;

public interface GptService {
    ResponseEntity<?> getAssistantMsg(String userMsg) throws Exception;
}
