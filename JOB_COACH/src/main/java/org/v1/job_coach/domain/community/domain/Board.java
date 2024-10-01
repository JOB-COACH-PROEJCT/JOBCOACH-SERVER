package org.v1.job_coach.domain.community.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.v1.job_coach.domain.community.dto.request.BoardChangeRequestDto;
import org.v1.job_coach.domain.community.dto.response.BoardResponseDto;
import org.v1.job_coach.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    @Column
    private LocalDateTime createDate;

    @Column
    private LocalDateTime updateDate;

    @ManyToOne //다대일 관계, 여러 게시물이 하나의 baybLion의 것
    @JoinColumn(name = "user_pid")
    @JsonBackReference // 이 Board가 속한 User 정보를 직렬화에서 제외
    private User user;

    @JsonIgnore // 순환 참조 방지
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;


    public BoardResponseDto updateBoard(BoardChangeRequestDto boardChangeRequestDto) {
        this.title = boardChangeRequestDto.title();
        this.content = boardChangeRequestDto.content();
        return BoardResponseDto.toDto(this);
    }

    public Board(Long id, String title, LocalDateTime createDate) {
        this.id = id;
        this.title = title;
        this.createDate = createDate;
    }


}
