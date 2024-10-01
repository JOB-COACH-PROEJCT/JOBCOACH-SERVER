package org.v1.job_coach.domain.chatroom.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.v1.job_coach.domain.chatroom.domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT q FROM Question q ORDER BY RAND()")
    Page<Question> findRandomQuestion(Pageable pageable);
}
