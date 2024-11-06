package org.v1.job_coach.domain.chatroom.application.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.v1.job_coach.domain.answer.dao.AnswerRepository;
import org.v1.job_coach.domain.answer.domain.Answer;
import org.v1.job_coach.domain.answer.dto.request.AnswerRequestDto;
import org.v1.job_coach.domain.chatroom.application.ChatRoomService;
import org.v1.job_coach.domain.chatroom.dao.ChatRoomRepository;
import org.v1.job_coach.domain.chatroom.dao.QuestionRepository;
import org.v1.job_coach.domain.chatroom.domain.ChatRoom;
import org.v1.job_coach.domain.chatroom.domain.ChatRoomStatus;
import org.v1.job_coach.domain.chatroom.domain.Question;
import org.v1.job_coach.domain.chatroom.dto.response.AnswerResponseDto;
import org.v1.job_coach.domain.chatroom.dto.response.ChatRoomResponseDto;
import org.v1.job_coach.domain.chatroom.dto.response.QuestionResponseDto;
import org.v1.job_coach.domain.consulting.application.ConsultingService;
import org.v1.job_coach.domain.consulting.dao.ConsultingRepository;
import org.v1.job_coach.domain.consulting.domain.Consulting;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.user.dao.UserRepository;
import org.v1.job_coach.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final QuestionRepository questionRepository;
    private final ConsultingRepository consultingRepository;
    private final AnswerRepository answerRepository;
    private final ConsultingService consultingService;

    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository, QuestionRepository questionRepository, ConsultingRepository consultingRepository, AnswerRepository answerRepository, UserRepository userRepository, ConsultingService consultingService) {
        this.chatRoomRepository = chatRoomRepository;
        this.questionRepository = questionRepository;
        this.consultingRepository = consultingRepository;
        this.answerRepository = answerRepository;
        this.consultingService = consultingService;
    }

    @Override
    @Transactional
    public ResultResponseDto<Page<?>> getChatRooms(User user, int page){
        Pageable pageable = PageRequest.of(page, 10);
        /*사용자 ID로 ChatRoom 리스트 가져오기 (페이징 적용)*/
        Page<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByUserPid(user.getPid(), pageable);
        /* ChatRoom 리스트를 ChatRoomResponseDto로 변환 */
        List<ChatRoomResponseDto> chatRoomsList = chatRooms.getContent()
                .stream()
                .map(ChatRoomResponseDto::toDto)
                .toList();

        if (page >= chatRooms.getTotalPages()){
            throw new CustomException(Error.INVALID_PAGE);
        }

        return ResultResponseDto.toDataResponseDto(
                200,
                "채팅방 목록이 성공적으로 반환되었습니다.",
                new PageImpl<>(chatRoomsList, pageable, chatRoomsList.size()));
    }

    @Override
    @Transactional
    public ResultResponseDto<?> createChatRoom(User user, String roomName) {
        ChatRoom chatRoom = new ChatRoom(roomName, user, ChatRoomStatus.ACTIVE);
        chatRoomRepository.save(chatRoom);
        Question question = getRandomQuestionFromRepository();
        // 처음 질문 던지기
        chatRoom.addQuestion(question); // 첫 질문을 추가

        return ResultResponseDto.toResultResponseDto(201, "채팅방이 성공적으로 생성되었습니다.");
    }

    @Override
    @Transactional
    public ResultResponseDto<?> deactivateChatRoom(User user, Long chatRoomId) {
        extracted(user, chatRoomId);
        ChatRoom chatRoom = findChatRoomOrThrow(chatRoomId);
        chatRoom.changeChatRoomState(ChatRoomStatus.COMPLETED);

        return ResultResponseDto.toResultResponseDto(200, "채팅방이 성공적으로 비활성화되었습니다.");
    }

    @Override
    @Transactional
    public AnswerResponseDto saveAnswer(User user, AnswerRequestDto answerRequestDto, Long chatRoomId) {
        ChatRoom chatRoom = findChatRoomOrThrow(chatRoomId);
        Question question = findQuestion(answerRequestDto.questionId());
        extracted(user, chatRoomId);

        Answer answer = new Answer(answerRequestDto.answerContent(), chatRoom, question, user);
        question.addAnswer(answer);
        answerRepository.save(answer);

        /*CompletableFuture<String> stringCompletableFuture = consultingService.processConsulting(answer.getId());*/
        // 비동기적으로 Consulting 처리
        consultingService.processConsulting(answer.getId())
                .thenAccept(consultingResult -> {
                    // Consulting 생성 및 저장 전 로깅 추가
                    log.info("비동기 작업 완료 후 Consulting 객체 생성 시작. Answer ID: {}, Consulting 결과: {}", answer.getId(), consultingResult);

                    Consulting consulting = new Consulting(
                            answer.getChatRoom(),
                            answer.getQuestion(),
                            answer, consultingResult,
                            answer.getUser());

                    answer.addConsulting(consulting);

                    log.info("Consulting 객체 생성 완료. Consulting 정보: {}", consulting);

                    consultingRepository.save(consulting);

                    // Consulting 저장 후 로깅 추가
                    log.info("Consulting 객체 저장 완료. Consulting ID: {}", consulting.getId());
                });
        // 다음 질문 던지기
        askNextQuestion(user, chatRoom);
        return AnswerResponseDto.toAnswerResponseDto(answer);

    }


    @Override
    @Transactional
    public ResultResponseDto<?> deleteChatRoom(User user, Long chatRoomId) {
        extracted(user, chatRoomId);
        ChatRoom chatRoom = findChatRoomOrThrow(chatRoomId);
        chatRoomRepository.delete(chatRoom);

        return ResultResponseDto.toResultResponseDto(
                200,
                "채팅방이 성공적으로 삭제되었습니다.");
    }

    @Transactional
    public ResultResponseDto<?> getQuestion(User user, Long chatRoomId) {
        extracted(user, chatRoomId);
        Question question = getRandomQuestionFromRepository();

        QuestionResponseDto questionResponse = QuestionResponseDto.toDto(question);
        return ResultResponseDto.toDataResponseDto(
                200,
                "질문이 성공적으로 반환되었습니다.",
                questionResponse);
    }


    private ChatRoom findChatRoomOrThrow(Long chatRoomId) {
        return isChatRoomPresent(chatRoomId);
    }

    private Question findQuestion(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_QUESTION));
    }

    private Answer findAnswer(Long answerId) {
        return answerRepository.findById(answerId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_QUESTION));
    }

    private void askNextQuestion(User user, ChatRoom chatRoom) {
        Question nextQuestion = getRandomQuestionFromRepository();
        chatRoom.addQuestion(nextQuestion); // 채팅방에 질문 추가
    }

    /**
     * Repository에서 랜덤 질문을 가져오는 공용 메서드
     */
    private Question getRandomQuestionFromRepository() {
        return questionRepository.findRandomQuestion(PageRequest.of(0, 1))
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_QUESTION));
    }


    private ChatRoom isChatRoomPresent(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_CHATROOM));
    }

    private void extracted(User user, Long chatRoomId) {
        Long chatRoomCreatedUserPid = isChatRoomPresent(chatRoomId).getUser().getPid();
        if (!user.getPid().equals(chatRoomCreatedUserPid)) {
            throw new CustomException(Error.ACCESS_DENIED);
        }
    }

    @Transactional
    public ResultResponseDto<?> toSaveAnswerResponseDto(Long answerId) {
        findAnswer(answerId);
        return ResultResponseDto.toResultResponseDto(
                201,
                "답변이 성공적으로 저장되었습니다.");
    }

    @Transactional
    public void consultingInjection(Long answerId, Consulting consulting) {
        Answer answer = findAnswer(answerId);
        answer.addConsulting(consulting);
    }


}
