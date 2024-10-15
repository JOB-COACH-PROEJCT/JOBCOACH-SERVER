package org.v1.job_coach.domain.chat.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.v1.job_coach.user.domain.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "ChattingRoom")
@DynamicUpdate
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(value = {AuditingEntityListener.class})
@NoArgsConstructor
public class ChattingRoom {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private String id;

    //단방향
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "lastChatMesg_id")
    private ChatMessage lastChatMsg;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "ChatRoom_Members",
            joinColumns = @JoinColumn(name = "chatRoom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> chatRoomMembers = new HashSet<>();

    @Column(name = "createdAt", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    public static ChattingRoom create() {
        ChattingRoom room = new ChattingRoom();
        room.randomId(UUID.randomUUID().toString());
        room.createdAt = LocalDateTime.now();
        return room;
    }

    private void randomId(String randomId) {
        this.id = randomId;
    }

    public void addMembers(User roomMaker, User guest) {
        this.chatRoomMembers.add(roomMaker);
        this.chatRoomMembers.add(guest);
    }

    public boolean isMember(User user) {
        return this.chatRoomMembers.contains(user);
    }
    public void lastChatMsg(ChatMessage chatMessage) {
        this.lastChatMsg = chatMessage;
    }
}