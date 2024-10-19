package org.v1.job_coach.domain.chat.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.v1.job_coach.coach.domain.Coach;
import org.v1.job_coach.user.domain.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Slf4j
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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "chat_room_members",
            joinColumns = @JoinColumn(name = "chat_room_id"),
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

    public void addMembers(User user, Coach coach) {
        log.info("[ChattingRoom] addMembers - User: {}, Coach: {}", user.getPid(), coach.getPid());
        this.chatRoomMembers.add(user);
        this.chatRoomMembers.add(coach);
        log.info("[ChattingRoom] addMembers 주입 완료 - User: {}, Coach: {}", this.chatRoomMembers.add(user), this.chatRoomMembers.add(coach));

    }

    public boolean isMember(User user) {
        return this.chatRoomMembers.contains(user);
    }
    public void lastChatMsg(ChatMessage chatMessage) {
        this.lastChatMsg = chatMessage;
    }
}