package org.v1.job_coach.domain.chatroom.application.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v1.job_coach.domain.answer.dao.AnswerRepository;
import org.v1.job_coach.domain.answer.domain.Answer;
import org.v1.job_coach.domain.answer.dto.request.AnswerRequestDto;
import org.v1.job_coach.domain.chatroom.application.ChatRoomService;
import org.v1.job_coach.domain.chatroom.dao.ChatRoomRepository;
import org.v1.job_coach.domain.chatroom.dao.QuestionRepository;
import org.v1.job_coach.domain.chatroom.domain.ChatRoom;
import org.v1.job_coach.domain.chatroom.domain.ChatRoomStatus;
import org.v1.job_coach.domain.chatroom.domain.Question;
import org.v1.job_coach.domain.chatroom.dto.response.ChatRoomResponseDto;
import org.v1.job_coach.domain.chatroom.dto.response.QuestionResponseDto;
import org.v1.job_coach.domain.consulting.domain.Consulting;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.user.dao.UserRepository;
import org.v1.job_coach.user.domain.User;

import java.util.List;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }


    @Override
    @Transactional
    public void questionList() {

    }

    @Override
    @Transactional
    public ChatRoom createChatRoom(User user, String roomName) {
        ChatRoom chatRoom = new ChatRoom(roomName, user, ChatRoomStatus.ACTIVE);
        chatRoomRepository.save(chatRoom);
        Question question = getQuestion(user, chatRoom.getId());
        // 처음 질문 던지기
        chatRoom.addQuestion(question); // 첫 질문을 추가
        return chatRoom;
    }
    @Override
    @Transactional
    public void deactivateChatRoom(User user, Long chatRoomId) {
        extracted(user, chatRoomId);
        ChatRoom chatRoom = findChatRoomOrThrow(chatRoomId);
        chatRoom.changeChatRoomState(ChatRoomStatus.COMPLETED);
    }
    @Override
    @Transactional
    public QuestionResponseDto getRandomQuestion(User user, Long chatRoomId) {
        extracted(user, chatRoomId);
        return QuestionResponseDto.toDto(questionRepository.findRandomQuestion(PageRequest.of(0, 1)).getContent().get(0));
    }

    @Override
    @Transactional
    public Answer saveAnswer(User user, AnswerRequestDto answerRequestDto, Long chatRoomId) {
        ChatRoom chatRoom = findChatRoomOrThrow(chatRoomId);
        Question question = findQuestion(answerRequestDto.questionId());
        extracted(user, chatRoomId);
        Answer answer = new Answer(answerRequestDto.answerContent(), chatRoom, question, user);
        question.addAnswer(answer);
        answerRepository.save(answer);
        // 다음 질문 던지기
        askNextQuestion(user, chatRoom);

        return answer;
    }
    @Override
    @Transactional
    public void deleteChatRoom(User user, Long chatRoomId) {
        extracted(user, chatRoomId);
        ChatRoom chatRoom = findChatRoomOrThrow(chatRoomId);
        extracted(user, chatRoomId);
        chatRoomRepository.delete(chatRoom);
    }
    @Override
    @Transactional
    public Page<ChatRoomResponseDto> getChatRooms(User user, int page){
        Pageable pageable = PageRequest.of(page, 10);
        /*사용자 ID로 ChatRoom 리스트 가져오기 (페이징 적용)*/
        Page<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByUserPid(user.getPid(), pageable);
        /* ChatRoom 리스트를 ChatRoomResponseDto로 변환 */
        List<ChatRoomResponseDto> chatRoomsList = chatRooms.getContent()
                .stream()
                .map(ChatRoomResponseDto::toDto)
                .toList();
        return new PageImpl<>(chatRoomsList, pageable, chatRoomsList.size());
    }

    @Transactional
    public void consultingInjection(Answer answer, Consulting consulting) {
        answer.addConsulting(consulting);
    }

    private ChatRoom findChatRoomOrThrow(Long chatRoomId) {
        return isChatRoomPresent(chatRoomId);
    }

    private Question findQuestion(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_QUESTION));
    }
    private void askNextQuestion(User user, ChatRoom chatRoom) {
        Question nextQuestion = getQuestion(user, chatRoom.getId());// 새로운 랜덤 질문 가져오기
        chatRoom.addQuestion(nextQuestion); // 채팅방에 질문 추가
    }

    private Question getQuestion(User user, Long chatRoomId) {
        QuestionResponseDto randomQuestion = getRandomQuestion(user, chatRoomId);
        return questionRepository.findById(randomQuestion.id()).orElseThrow(() -> new CustomException(Error.NOT_FOUND_QUESTION));
    }

    private ChatRoom isChatRoomPresent(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_CHATROOM));
    }

    public void extracted(User user, Long chatRoomId) {
        Long chatRoomCreatedUserPid = isChatRoomPresent(chatRoomId).getUser().getPid();
        if (!user.getPid().equals(chatRoomCreatedUserPid)) {
            throw new CustomException(Error.ACCESS_DENIED);
        }
    }

}
