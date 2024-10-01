package org.v1.job_coach.entity.community;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.v1.job_coach.dto.board.BoardChangeDto;
import org.v1.job_coach.dto.board.BoardRequestDto;
import org.v1.job_coach.dto.board.BoardResponseDto;
import org.v1.job_coach.entity.User;

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


    public BoardResponseDto updateBoard(BoardChangeDto boardChangeDto) {
        this.title = boardChangeDto.title();
        this.content = boardChangeDto.content();
        return BoardResponseDto.toDto(this);
    }

    public Board(Long id, String title, LocalDateTime createDate) {
        this.id = id;
        this.title = title;
        this.createDate = createDate;
    }


}
