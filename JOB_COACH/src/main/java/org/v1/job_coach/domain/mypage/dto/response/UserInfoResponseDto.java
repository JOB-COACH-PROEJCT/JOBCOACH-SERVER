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
        @Schema(description = "회원 이름", example = "서이름")
        String name,
        @Schema(description = "회원 이메일", example = "ggowater112@naver.com")
        String email,
        @Schema(description = "회원 전화번호", example = "01053636177")
        String phoneNumber,
        @Schema(description = "회원 프로필 Url", example = "https://i.pinimg.com/222x/9e/06/9a/9e069a636dd2bb4eef3290a546648379.jpg")
        String profileUrl,
        @Schema(description = "회원 모의면접 채팅방 목록", example = "")
        List<ChatRoom> chatRooms,
        @Schema(description = "회원 커뮤니티 작성 게시글 목록", example = "")
        List<Board> boards,
        @Schema(description = "회원 면접후기 작성 목록", example = "")
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
