package org.v1.job_coach.coach.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.v1.job_coach.domain.chat.domain.ChattingRoom;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.user.domain.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    @Enumerated(EnumType.STRING)
    private MatchingStatus status;

    @Column
    private LocalDateTime scheduledAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chatting_room_id")
    private ChattingRoom chattingRoom;

    public Matching(User user, Coach coach) {
        this.user = user;
        this.coach = coach;
        this.requestedAt = LocalDateTime.now();
        this.status = MatchingStatus.REQUESTED;
    }
    public void createChattingRoom(ChattingRoom newRoom) {
        this.chattingRoom = newRoom;
    }

    public void accept() {
        if (this.status != MatchingStatus.REQUESTED) {
            throw new CustomException(Error.MATCHING_NOT_REQUESTED);}
        this.status = MatchingStatus.APPROVED;
    }

    public void reject() {
        if (this.status != MatchingStatus.REQUESTED) {
            throw new CustomException(Error.MATCHING_NOT_REQUESTED);}
        this.status = MatchingStatus.REJECTED;
    }

    public void cancel() {
        if (this.status == MatchingStatus.APPROVED) {
            throw new CustomException(Error.MATCHING_ALREADY_APPROVED);}
        this.status = MatchingStatus.CANCELLED;
    }

    public void complete() {
        if (this.status != MatchingStatus.APPROVED) {
            throw new CustomException(Error.MATCHING_NOT_APPROVED);}
        this.status = MatchingStatus.COMPLETED;
    }

}
