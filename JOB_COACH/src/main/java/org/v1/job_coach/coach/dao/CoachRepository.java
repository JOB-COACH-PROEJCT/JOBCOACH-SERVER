package org.v1.job_coach.coach.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.v1.job_coach.coach.domain.Coach;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {

    Page<Coach> findAllBy(Pageable pageable);
}
