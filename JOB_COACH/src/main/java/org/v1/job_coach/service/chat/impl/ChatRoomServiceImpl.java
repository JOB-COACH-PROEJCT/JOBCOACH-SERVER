package org.v1.job_coach.service.chat.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v1.job_coach.dto.chat.AnswerRequestDto;
import org.v1.job_coach.dto.chat.ChatRoomResponseDto;
import org.v1.job_coach.dto.chat.QuestionResponseDto;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.entity.chat.Answer;
import org.v1.job_coach.entity.chat.ChatRoom;
import org.v1.job_coach.entity.chat.ChatRoomStatus;
import org.v1.job_coach.entity.chat.Question;
import org.v1.job_coach.entity.consulting.Consulting;
import org.v1.job_coach.exception.CustomException;
import org.v1.job_coach.exception.Error;
import org.v1.job_coach.repository.UserRepository;
import org.v1.job_coach.repository.chat.AnswerRepository;
import org.v1.job_coach.repository.chat.ChatRoomRepository;
import org.v1.job_coach.repository.chat.QuestionRepository;
import org.v1.job_coach.service.chat.ChatRoomService;

import java.util.List;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
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
        Question question = getQuestion();
        // 처음 질문 던지기
        chatRoom.addQuestion(question); // 첫 질문을 추가
        return chatRoom;
    }
    @Override
    @Transactional
    public void deactivateChatRoom(User user, Long chatRoomId) {
        ChatRoom chatRoom = findChatRoomOrThrow(chatRoomId);
        chatRoom.changeChatRoomState(ChatRoomStatus.COMPLETED);
    }
    @Override
    @Transactional
    public QuestionResponseDto getRandomQuestion() {
        return QuestionResponseDto.toDto(questionRepository.findRandomQuestion(PageRequest.of(0, 1)).getContent().get(0));
    }

    @Override
    @Transactional
    public Answer saveAnswer(User user, AnswerRequestDto answerRequestDto, Long chatRoomId) {
        ChatRoom chatRoom = findChatRoomOrThrow(chatRoomId);
        Question question = findQuestionOrThrow(answerRequestDto.questionId());
        isOwner(chatRoom, user);

        Answer answer = new Answer(answerRequestDto.answerContent(), chatRoom, question, user);
        question.addAnswer(answer);
        answerRepository.save(answer);
        // 다음 질문 던지기
        askNextQuestion(chatRoom);

        return answer;
    }
    @Override
    @Transactional
    public void deleteChatRoom(User user, Long chatRoomId) {
        ChatRoom chatRoom = findChatRoomOrThrow(chatRoomId);
        isOwner(chatRoom, user);
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
    private void isOwner(ChatRoom chatRoom, User user) {
        if (!chatRoom.isOwner(user)) {
            throw new CustomException(Error.NOT_AUTHORIZED);
        }
    }

    private ChatRoom findChatRoomOrThrow(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_CHATROOM));
    }
    private Question findQuestionOrThrow(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_QUESTION));
    }
    private void askNextQuestion(ChatRoom chatRoom) {
        Question nextQuestion = getQuestion();// 새로운 랜덤 질문 가져오기
        chatRoom.addQuestion(nextQuestion); // 채팅방에 질문 추가
    }

    private Question getQuestion() {
        QuestionResponseDto randomQuestion = getRandomQuestion();
        Question question = questionRepository.findById(randomQuestion.id()).orElseThrow(() -> new CustomException(Error.NOT_FOUND_QUESTION));
        return question;
    }

}
