package org.v1.job_coach.domain.chatroom.dto.response;

import org.v1.job_coach.domain.answer.domain.Answer;
import org.v1.job_coach.domain.chatroom.domain.ChatRoom;
import org.v1.job_coach.domain.chatroom.domain.ChatRoomQuestion;
import org.v1.job_coach.domain.chatroom.domain.ChatRoomStatus;

import java.util.ArrayList;
import java.util.List;

public record DetailsChatRoomResponseDto(
        Long id,
        String roomName,
        Long userPid,
        String userName,
        ChatRoomStatus status,
        List<String> answers,
        List<String> questions
) {

    public static DetailsChatRoomResponseDto toDto(ChatRoom chatRoom) {
        List<String> questionContentList = new ArrayList<>(); // 초기화
        List<String> answerContentList = new ArrayList<>(); // 초기화
        List<Answer> answerList = chatRoom.getAnswerList();
        List<ChatRoomQuestion> chatRoomQuestions = chatRoom.getChatRoomQuestions();
        for(Answer answer : answerList){
            if (!(answer == null)) {
                answerContentList.add(answer.getContent());
            }
        }
        for (ChatRoomQuestion chatRoomQuestion : chatRoomQuestions) {
            if (!(chatRoomQuestion == null)) {
                questionContentList.add(chatRoomQuestion.getQuestion().getContent());
            }
        }
        return new DetailsChatRoomResponseDto(
                chatRoom.getId(),
                chatRoom.getRoomName(),
                chatRoom.getUser().getUserPid(),
                chatRoom.getUser().getName(),
                chatRoom.getStatus(),
                answerContentList,
                questionContentList
        );
    }
}
