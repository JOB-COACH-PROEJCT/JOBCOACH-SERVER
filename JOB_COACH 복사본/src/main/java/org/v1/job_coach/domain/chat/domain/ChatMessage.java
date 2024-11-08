package org.v1.job_coach.domain.chat.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.v1.job_coach.global.util.DateFormatter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "ChatMessage")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(value = {AuditingEntityListener.class})
@NoArgsConstructor
public class ChatMessage {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private String roomId; //단순히 ID 값만 필요  (ChatRoom)

    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private String authorId; //단순히 ID 값만 필요 (User)


    @Column(name = "message")
    private String message;

    @Column(name = "author_name")
    private String authorName; // 사용자 이름 추가

    @Column(name = "profile_image_url")
    private String profileImageUrl; // 프로필 이미지 URL 추가

    @Column(name = "createdAt", updatable = false)
    @CreatedDate
    private String createdAt;

    public ChatMessage(String roomId, String authorId, String authorName,String profileImageUrl, String message) {
        this.roomId = roomId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.profileImageUrl = profileImageUrl;
        this.message = message;
        this.createdAt = DateFormatter.getDateNow(LocalDateTime.now());
    }


}