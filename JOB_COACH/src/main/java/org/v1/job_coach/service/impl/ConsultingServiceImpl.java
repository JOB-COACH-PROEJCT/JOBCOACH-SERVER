package org.v1.job_coach.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.v1.job_coach.entity.chat.Answer;
import org.v1.job_coach.entity.chat.Consulting;
import org.v1.job_coach.exception.CustomException;
import org.v1.job_coach.exception.Error;
import org.v1.job_coach.repository.chat.AnswerRepository;
import org.v1.job_coach.repository.chat.ConsultingRepository;
import org.v1.job_coach.service.ConsultingService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ConsultingServiceImpl implements ConsultingService {
    @Value("${openai.api.secret}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String url;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AnswerRepository answerRepository;
    private final ConsultingRepository consultingRepository;

    public ConsultingServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper, AnswerRepository answerRepository, ConsultingRepository consultingRepository) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.answerRepository = answerRepository;
        this.consultingRepository = consultingRepository;
    }

    public Consulting processConsulting(Answer answer) throws Exception {
        // Answer 객체 조회
        answerRepository.findById(answer.getId()).orElseThrow(() -> new CustomException(Error.NOT_FOUND_ANSWER));

        Map<String, Object> responseMap = callChatGpt(answer);
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");

        Consulting consulting;
        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String content = (String) message.get("content");
            /* Consulting 객체 생성 및 저장 */
            consulting = new Consulting(answer, content);
            consultingRepository.save(consulting);
        } else {
            log.error("[(ConsultingService.java) processConsulting] OpenAI API 응답 오류");
            throw new CustomException(Error.ERROR_OPENAI_RESPONSE);
        }
        return consulting;
    }
    public Map<String, Object> callChatGpt(Answer answer) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        log.info("[callChatGpt] apiKey = {}", apiKey);
        log.info("[callChatGpt] model = {}", model);

        Map<String, Object> bodyMap = createConsulting(answer, model);
        String body = objectMapper.writeValueAsString(bodyMap);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            return objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to call OpenAI API", e);
        }
    }

    //
    public Map<String, Object> createConsulting(Answer answer, String model) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("model", model);
        String systemEx =
                "Always follow these rules: " +
                        "You are an AI for interview consulting, so only handle interview-related questions. For answers that are not interview-related, respond with 'This is not a relevant answer to the interview.' " +
                        "For the question " + answer.getQuestion() + "determine if the user's answer is appropriate. " +
                        "Here’s what to avoid and what to include: " +
                        "Avoid: Using first-person pronouns like 'I', 'my' unless absolutely necessary. " +
                        "Using terms like 'your company' instead of the company's official name. " +
                        "Abbreviations. " +
                        "Speculative language; be clear and definitive. " +
                        "Short, one-line answers. " +
                        "Include: " +
                        "Stories related to your experience. " +
                        "Interest in the company. " +
                        "Confident expressions. " +
                        "Avoid short answers. " +
                        "Provide feedback on what needs improvement and what is done well based on the interview answer. " +
                        "Respond in Korean.";

        List<Map<String, String>> messages = List.of(
                Map.of("role", "user", "content", answer.getContent()),
                Map.of("role", "system", "content", systemEx)
        );

        bodyMap.put("messages", messages);
        return bodyMap;
    }
}
