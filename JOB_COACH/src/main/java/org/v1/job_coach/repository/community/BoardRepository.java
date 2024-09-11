package org.v1.job_coach.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.v1.job_coach.entity.community.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}
