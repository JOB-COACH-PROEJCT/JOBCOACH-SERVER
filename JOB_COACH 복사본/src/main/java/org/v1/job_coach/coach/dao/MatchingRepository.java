package org.v1.job_coach.coach.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.v1.job_coach.coach.domain.Coach;
import org.v1.job_coach.coach.domain.Matching;
import org.v1.job_coach.user.domain.User;

import java.util.Optional;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
    Page<Matching> findByUser(User user, Pageable pageable);

    Page<Matching> findByCoach(Coach coach, Pageable pageable);

    Optional<Matching> findByUser_PidAndCoach_Pid(Long userId, Long coachId);


}
