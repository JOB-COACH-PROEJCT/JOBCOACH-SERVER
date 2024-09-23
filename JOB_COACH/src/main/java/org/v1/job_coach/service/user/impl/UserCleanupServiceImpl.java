package org.v1.job_coach.service.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
public class UserCleanupServiceImpl {

    private final UserRepository userRepository;
    public UserCleanupServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void deleteInactiveUsers() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minus(3, ChronoUnit.MONTHS);
        List<User> usersToDelete = userRepository.findByIsActiveFalseAndDeactivatedAtBefore(threeMonthsAgo);
        log.info("삭제할 사용자 수: {}", usersToDelete.size());
        userRepository.deleteByIsActiveFalseAndDeactivatedAtBefore(threeMonthsAgo);
    }

    @Transactional
    //@Scheduled(cron = "*/10 * * * * ?") // 매 10초마다 실행
    public void deleteInactiveUsersTest() {
        LocalDateTime tenSecondsAgo = LocalDateTime.now().minusSeconds(10);
        List<User> usersToDelete = userRepository.findByIsActiveFalseAndDeactivatedAtBefore(tenSecondsAgo);
        log.info("삭제할 사용자 수: {}", usersToDelete.size());
        userRepository.deleteByIsActiveFalseAndDeactivatedAtBefore(tenSecondsAgo);
    }

}
