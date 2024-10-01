package org.v1.job_coach.domain.consulting.application.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.v1.job_coach.domain.answer.dao.AnswerRepository;
import org.v1.job_coach.domain.answer.domain.Answer;
import org.v1.job_coach.domain.chatroom.dao.ChatRoomRepository;
import org.v1.job_coach.domain.chatroom.domain.ChatRoom;
import org.v1.job_coach.domain.consulting.application.ConsultingService;
import org.v1.job_coach.domain.consulting.dao.ConsultingRepository;
import org.v1.job_coach.domain.consulting.domain.Consulting;
import org.v1.job_coach.domain.consulting.dto.response.ConsultingResponseDto;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.user.dao.UserRepository;
import org.v1.job_coach.user.domain.User;

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
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ConsultingServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper, AnswerRepository answerRepository, ConsultingRepository consultingRepository, ChatRoomRepository chatRoomRepository, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.answerRepository = answerRepository;
        this.consultingRepository = consultingRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Consulting processConsulting(Answer answer) throws Exception {
        // Answer 객체 조회
        answerRepository.findById(answer.getId()).orElseThrow(() -> new CustomException(Error.NOT_FOUND_ANSWER));

        Map<String, Object> responseMap = callChatGpt(answer);
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");

        Consulting consulting;
        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String content = (String) message.get("content");

            if (content.length() > 2000) {
                log.info("[content 2000자 초과]content: {}", content);
                log.info("[content 2000자 초과]substring을 시작합니다.");
                content = content.substring(0, 2000);
            }

            /* Consulting 객체 생성 및 저장 */
            consulting = new Consulting(answer.getChatRoom(), answer.getQuestion(), answer, content, answer.getUser());
            consultingRepository.save(consulting);
        } else {
            log.error("[(ConsultingService.java) processConsulting] OpenAI API 응답 오류");
            throw new CustomException(Error.ERROR_OPENAI_RESPONSE);
        }
        return consulting;
    }

    @Transactional
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

    @Transactional
    public Map<String, Object> createConsulting(Answer answer, String model) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("model", model);
        log.info("Consulting 시작");
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

    @Transactional
    public Page<ConsultingResponseDto> getConsultingByChatRoom(Long roomId, int page, User user) {
        ChatRoom chatRoom = validateChatRoomOwnership(roomId, user);
        if (page < 0 || page > (chatRoom.getAnswerList().size() - 1)) {
            throw new CustomException(Error.INVALID_PAGE);
        }
        Pageable pageable = PageRequest.of(page, 1);
        Page<Consulting> consultingPage = consultingRepository.findByChatRoomIdAndUser(roomId, user, pageable);
        return consultingPage.map(consulting -> ConsultingResponseDto.toDto(
                consulting.getQuestion(),
                consulting.getAnswer(),
                consulting));
    }
    private ChatRoom validateChatRoomOwnership(Long roomId, User user) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new CustomException(Error.NOT_FOUND_CHATROOM));
        User owner = userRepository.findById(user.getPid()).orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
        if (!owner.getPid().equals(chatRoom.getUser().getPid())) {
            throw new CustomException(Error.NOT_AUTHORIZED);
        }
        return chatRoom;
    }
}