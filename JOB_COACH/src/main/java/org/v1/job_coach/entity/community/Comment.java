package org.v1.job_coach.entity.community;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.v1.job_coach.dto.comment.CommentRequestDto;
import org.v1.job_coach.dto.comment.CommentResponseDto;
import org.v1.job_coach.entity.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // 이 Comment가 속한 User 정보를 직렬화에서 제외
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @JsonBackReference
    private Board board;

    @Column(nullable = false)
    private String content;

    private LocalDateTime createdAt;

    public Comment(CommentRequestDto commentRequestDto, User user, Board board) {
        this.user = user;
        this.board = board;
        this.content = commentRequestDto.content();
        this.createdAt = LocalDateTime.now();
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
