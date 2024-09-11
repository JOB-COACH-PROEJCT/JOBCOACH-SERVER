package org.v1.job_coach.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.v1.job_coach.entity.chat.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
