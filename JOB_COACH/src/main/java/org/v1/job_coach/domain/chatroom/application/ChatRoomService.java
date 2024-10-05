package org.v1.job_coach.domain.chatroom.application;

import org.springframework.data.domain.Page;
import org.v1.job_coach.domain.answer.domain.Answer;
import org.v1.job_coach.domain.answer.dto.request.AnswerRequestDto;
import org.v1.job_coach.domain.chatroom.domain.ChatRoom;
import org.v1.job_coach.domain.chatroom.domain.Question;
import org.v1.job_coach.domain.chatroom.dto.response.AnswerResponseDto;
import org.v1.job_coach.domain.chatroom.dto.response.ChatRoomResponseDto;
import org.v1.job_coach.domain.chatroom.dto.response.QuestionResponseDto;
import org.v1.job_coach.domain.consulting.domain.Consulting;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.user.domain.User;

public interface ChatRoomService {

    ResultResponseDto<Page<?>> getChatRooms(User user, int page);

    /* 새로운 채팅방 생성하기 -> 채팅방 객체 생성 */
    ResultResponseDto<?> createChatRoom(User user, String roomName);

    ResultResponseDto<?> deactivateChatRoom(User user, Long ChatRoomId);

    ResultResponseDto<?> getQuestion(User user, Long chatRoomId);

    ResultResponseDto<?> deleteChatRoom(User user, Long chatRoomId);

    ResultResponseDto<?> toSaveAnswerResponseDto(Long answerId);

    AnswerResponseDto saveAnswer(User user, AnswerRequestDto answerRequestDto, Long chatRoomId);


     void consultingInjection(Long answerId, Consulting consulting);

}
