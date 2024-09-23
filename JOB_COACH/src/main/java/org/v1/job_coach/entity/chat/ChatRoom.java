package org.v1.job_coach.entity.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.v1.job_coach.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 이 채팅방을 생성한 사용자

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answerList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ChatRoomStatus status;

    public ChatRoom(String roomName, User user, List<Answer> answerList, ChatRoomStatus status) {
        this.roomName = roomName;
        this.user = user;
        this.answerList = answerList;
        this.status = status;
    }

    public ChatRoom(Long pid, String roomName) {
        this.id = pid;
        this.roomName = roomName;
    }

    public void changeChatRoomName(String roomName) {
        this.roomName = roomName;
    }

    /* ChatRoom에 사용자 검증 로직 추가 */
    public boolean isOwner(User user) {
        return this.user.getPid().equals(user.getPid()); // ID를 기준으로 비교
    }

    public void changeChatRoomState(ChatRoomStatus status) {
        this.status = status;
    }
}
