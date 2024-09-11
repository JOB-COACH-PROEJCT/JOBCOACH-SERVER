package org.v1.job_coach.service.chat.Impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v1.job_coach.dto.chat.AnswerRequestDto;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.entity.chat.*;
import org.v1.job_coach.exception.CustomException;
import org.v1.job_coach.exception.Error;
import org.v1.job_coach.repository.UserRepository;
import org.v1.job_coach.repository.chat.AnswerRepository;
import org.v1.job_coach.repository.chat.ChatRoomRepository;
import org.v1.job_coach.repository.chat.QuestionRepository;
import org.v1.job_coach.service.chat.InterViewService;

import java.util.ArrayList;

@Service
@Transactional
public class InterViewServiceImpl implements InterViewService {

    private final ChatRoomRepository chatRoomRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    public InterViewServiceImpl(ChatRoomRepository chatRoomRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void questionList() {

    }
    @Override
    public ChatRoom createChatRoom(User user, String roomName) {
        ChatRoom chatRoom = new ChatRoom(roomName, user, new ArrayList<>() , ChatRoomStatus.ACTIVE);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }
    @Override
    public void deactivateChatRoom(User user, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new CustomException(Error.NOT_FOUND_CHATROOM));
        chatRoom.changeChatRoomState(ChatRoomStatus.COMPLETED);
    }
    @Override
    public Question getRandomQuestion() {
        return questionRepository.findRandomQuestion(PageRequest.of(0, 1)).getContent().get(0);
    }
    @Override
    public void saveAnswer(User user, AnswerRequestDto answerRequestDto, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_CHATROOM));
        isOwner(chatRoom, user);
        Question question = questionRepository.findById(answerRequestDto.questionId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_QUESTION));
        Answer answer = new Answer(answerRequestDto.answerContent(), chatRoom, question, user);
        question.addAnswer(answer);
        answerRepository.save(answer);
    }
    @Override
    public void deleteChatRoom(User user, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new CustomException(Error.NOT_FOUND_CHATROOM));
        isOwner(chatRoom, user);
        chatRoomRepository.delete(chatRoom);
    }
    public void isOwner(ChatRoom chatRoom, User user) {
        if (!chatRoom.isOwner(user)) {
            throw new CustomException(Error.NOT_AUTHORIZED);
        }
    }
}
