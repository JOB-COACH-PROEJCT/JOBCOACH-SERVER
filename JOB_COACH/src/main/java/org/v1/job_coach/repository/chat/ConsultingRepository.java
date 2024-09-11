package org.v1.job_coach.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.v1.job_coach.entity.chat.Consulting;

@Repository
public interface ConsultingRepository extends JpaRepository<Consulting, Long> {
}
