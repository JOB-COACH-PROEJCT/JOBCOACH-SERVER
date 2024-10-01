package org.v1.job_coach.global.infra.service;

import org.springframework.http.ResponseEntity;

public interface GptService {
    ResponseEntity<?> getAssistantMsg(String userMsg) throws Exception;
}
