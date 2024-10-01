package org.v1.job_coach.global.infra.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.v1.job_coach.global.infra.service.GptService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class GptServiceImpl implements GptService {
    @Value("${openai.api.secret}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String url;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public GptServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    public Map<String, Object> callChatGpt(String userMsg) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        log.info("[callChatGpt] apiKey = {}", apiKey);
        log.info("[callChatGpt] model = {}", model);

        Map<String, Object> bodyMap = getStringObjectMap(userMsg, model);
        String body = objectMapper.writeValueAsString(bodyMap);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            return objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to call OpenAI API", e);
        }
    }

    private static Map<String, Object> getStringObjectMap(String userMsg, String model) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("model", model);

        List<Map<String, String>> messages = List.of(
                Map.of("role", "user", "content", userMsg),
                Map.of("role", "system", "content", "You are an AI that must always respond informally. and Always translate your answers into Korean before responding.")
        );

        bodyMap.put("messages", messages);
        return bodyMap;
    }

    @Override
    public ResponseEntity<?> getAssistantMsg(String userMsg) throws Exception {
        Map<String, Object> responseMap = callChatGpt(userMsg);
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String content = (String) message.get("content");
            return ResponseEntity.status(HttpStatus.OK).body(content);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No response from OpenAI API");
        }
    }
}
