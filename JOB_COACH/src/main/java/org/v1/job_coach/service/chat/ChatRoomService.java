package org.v1.job_coach.service.chat;

import org.springframework.data.domain.Page;
import org.v1.job_coach.dto.chat.AnswerRequestDto;
import org.v1.job_coach.dto.chat.ChatRoomResponseDto;
import org.v1.job_coach.dto.chat.QuestionResponseDto;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.entity.chat.Answer;
import org.v1.job_coach.entity.chat.ChatRoom;
import org.v1.job_coach.entity.consulting.Consulting;
import org.v1.job_coach.entity.chat.Question;

public interface ChatRoomService {

    /* 질문을 DB에 넣는 메서드 (테스트에만 사용 될 메서드)*/
    void questionList();

    Page<ChatRoomResponseDto> getChatRooms(User user, int page);

    /* 새로운 채팅방 생성하기 -> 채팅방 객체 생성 */
    ChatRoom createChatRoom(User user, String roomName);

    void deactivateChatRoom(User user, Long ChatRoomId);

    /* 랜덤한 질문 하나 가져오기 */
    QuestionResponseDto getRandomQuestion();

    /* 사용자 답변 저장하기 */
    Answer saveAnswer(User user, AnswerRequestDto answerRequestDto, Long chatRoomId);

    void deleteChatRoom(User user, Long chatRoomId);

    void consultingInjection(Answer answer, Consulting consulting);

}
