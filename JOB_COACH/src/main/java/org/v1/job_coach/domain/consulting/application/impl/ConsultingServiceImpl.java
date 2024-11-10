package org.v1.job_coach.domain.consulting.application.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
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
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.user.dao.UserRepository;
import org.v1.job_coach.user.domain.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<String> processConsulting(Long answerId) {
        log.info("[processConsulting] 비동기 메서드 시작, Answer ID: {}", answerId); // 호출 확인 로그

        try {
            Answer answer = answerRepository.findById(answerId)
                    .orElseThrow(() -> new CustomException(Error.NOT_FOUND_ANSWER));
            log.info("[processConsulting] Answer 조회 성공: {}", answer.getContent()); // Answer 조회 확인 로그

            Map<String, Object> responseMap = callChatGpt(answer);
            log.info("[processConsulting] API 호출 성공, 응답 데이터: {}", responseMap); // API 호출 성공 확인 로그

            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
            if (choices != null && !choices.isEmpty()) {
                String content = (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
                log.info("[processConsulting] 결과 데이터 생성: {}", content); // 결과 생성 로그

                return CompletableFuture.completedFuture(content);
            } else {
                log.error("[(ConsultingService.java) processConsulting] OpenAI API 응답 오류");
                throw new CustomException(Error.ERROR_OPENAI_RESPONSE);
            }
        } catch (Exception e) {
            log.error("[processConsulting] 중 예외 발생", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    public Map<String, Object> callChatGpt(Answer answer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        log.info("[callChatGpt] apiKey = {}", apiKey);
        log.info("[callChatGpt] model = {}", model);

        Map<String, Object> bodyMap = createConsulting(answer, model);
        String body = null;
        try {
            body = objectMapper.writeValueAsString(bodyMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            return objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to call OpenAI API", e);
        }
    }

    public Map<String, Object> createConsulting(Answer answer, String model) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("model", model);
        log.info("Consulting 시작");
        String systemEx =
                "Always follow these rules: " +
                        "You are an AI for interview consulting, so respond specifically to the user's question in the context of an interview. " +
                        "If " + answer.getContent() +"is an appropriate response to the question " + answer.getQuestion() + " , proceed with consulting. If the answer is entirely unrelated to the question, respond with '질문과 관련된 답변이 아니므로 컨설팅을 진행할 수 없습니다.'" +
                        "Provide feedback on the strengths and areas for improvement in the user's answer. Please proceed with consulting by referring to the following guidelines."+
                        "However, if the response generally aligns with the question and is relevant to interview consulting, proceed with a normal answer. " +
                        "Here are guidelines on what to avoid and include in your responses: " +
                        "Avoid: Using first-person pronouns like 'I', 'my' unless absolutely necessary. " +
                        "Avoid using terms like 'your company'; instead, use the company's official name. " +
                        "Avoid abbreviations. " +
                        "Avoid speculative language; be clear and definitive in your statements. " +
                        "Avoid short, one-line answers. " +
                        "Include: " +
                        "Relevant stories from your experience that relate to the question. " +
                        "Show interest in the company by mentioning specific details. " +
                        "Use confident expressions that convey assurance. " +
                        "Provide comprehensive feedback on areas that need improvement and aspects done well based on the interview response. " +
                        "Always respond in Korean.";




        List<Map<String, String>> messages = List.of(
                Map.of("role", "user", "content", answer.getContent()),
                Map.of("role", "system", "content", systemEx)
        );

        bodyMap.put("messages", messages);
        return bodyMap;
    }

    @Transactional
    public ResultResponseDto<?> getConsultingByChatRoom(Long roomId, int page, User user) {
        ChatRoom chatRoom = validateChatRoomOwnership(roomId, user);
        if (page < 0 || page > (chatRoom.getAnswerList().size() - 1)) {
            throw new CustomException(Error.INVALID_PAGE);
        }
        Pageable pageable = PageRequest.of(page, 1);
        Page<Consulting> consultingPage = consultingRepository.findByChatRoomIdAndUser(roomId, user, pageable);

        return ResultResponseDto.toDataResponseDto(200, "모의면접 리스트가 성공적으로 반환되었습니다.", consultingPage.map(consulting -> ConsultingResponseDto.toDto(
                consulting.getQuestion(),
                consulting.getAnswer(),
                consulting)));
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