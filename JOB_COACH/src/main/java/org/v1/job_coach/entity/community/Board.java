package org.v1.job_coach.entity.community;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.v1.job_coach.dto.board.BoardChangeDto;
import org.v1.job_coach.dto.board.BoardRequestDto;
import org.v1.job_coach.dto.board.BoardResponseDto;
import org.v1.job_coach.entity.User;

import java.time.LocalDateTime;

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
    @JsonIgnore
    private User user;


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
