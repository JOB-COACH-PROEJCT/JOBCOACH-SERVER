package org.v1.job_coach.domain.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.v1.job_coach.domain.chatroom.domain.ChatRoom;
import org.v1.job_coach.domain.community.domain.Board;
import org.v1.job_coach.domain.review.domain.Review;
import org.v1.job_coach.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "유저 정보 DTO")
public record UserInfoResponseDto(
        String name,
        String email,
        String phoneNumber,
        String profileUrl,
        List<ChatRoom> chatRooms,
        List<Board> boards,
        List<Review> reviews
) {
        //필요한 정보만 보내자!
        public static UserInfoResponseDto toDto(User user) {
                return new UserInfoResponseDto(
                        user.getName(),
                        user.getEmail(),
                        user.getNumber(),
                        user.getProfile(),
                        user.getChatRooms().stream()
                                .map(chatRoom -> new ChatRoom(chatRoom.getId(), chatRoom.getRoomName()))
                                .collect(Collectors.toList()),
                        user.getBoard().stream()
                                .map(board -> new Board(board.getId(), board.getTitle(), board.getCreateDate()))
                                .collect(Collectors.toList()),
                        user.getReviews().stream()
                                .map(review -> new Review(review.getId(), review.getTitle(), review.getCompanyName(), review.getCreateDate()))
                                .collect(Collectors.toList())
                );
    }
}
