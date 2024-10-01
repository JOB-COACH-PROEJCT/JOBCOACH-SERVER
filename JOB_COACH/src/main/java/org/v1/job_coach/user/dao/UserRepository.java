package org.v1.job_coach.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.v1.job_coach.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getByEmail(String email);

    //*비활성 사용자 및 가입일 기준 삭제*//*
    void deleteByIsActiveFalseAndDeactivatedAtBefore(LocalDateTime date);

    //*활성화된 이메일만 검색*//*
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email AND u.isActive = true")
    boolean existsByEmailAndIsActiveTrue(@Param("email") String email);

    List<User> findByIsActiveFalseAndDeactivatedAtBefore(LocalDateTime tenSecondsAgo);

    /*Page<ChatRoom> findChatRoomsByPid(Long userId, Pageable pageable);*/


}
