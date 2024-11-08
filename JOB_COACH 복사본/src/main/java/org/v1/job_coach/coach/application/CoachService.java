package org.v1.job_coach.coach.application;

import org.springframework.data.domain.Page;
import org.v1.job_coach.global.dto.response.ResultResponseDto;

public interface CoachService {

    ResultResponseDto<?> getAllCoaches(int page, int size);
}
