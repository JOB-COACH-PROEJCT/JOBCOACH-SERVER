package org.v1.job_coach.coach.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.v1.job_coach.coach.domain.Coach;
import org.v1.job_coach.coach.domain.Matching;
import org.v1.job_coach.coach.dto.MatchCoachResponseDto;
import org.v1.job_coach.coach.dto.MatchUserResponseDto;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.user.domain.User;

import java.util.List;

public interface MatchingService {
    ResultResponseDto<?> match(User user, Long coachId);

    ResultResponseDto<?> acceptMatch(User user, Long matchingId);

    ResultResponseDto<?> rejectMatch(User user, Long matchingId);

    ResultResponseDto<?> cancelMatch(User user, Long matchingId);

    ResultResponseDto<?> completeMatch(User user, Long matchingId);

    ResultResponseDto<?> userMatchingList(User user, int page);

    ResultResponseDto<?> coachMatchingList(User user, int page);
}
