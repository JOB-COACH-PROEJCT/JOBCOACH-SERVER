package org.v1.job_coach.domain.consulting.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.v1.job_coach.domain.consulting.domain.Consulting;
import org.v1.job_coach.user.domain.User;

@Repository
public interface ConsultingRepository extends JpaRepository<Consulting, Long> {
    Page<Consulting> findByChatRoomIdAndUser(Long chatRoomId, User user, Pageable pageable);
}
