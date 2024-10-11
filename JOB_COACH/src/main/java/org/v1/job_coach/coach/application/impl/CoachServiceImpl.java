package org.v1.job_coach.coach.application.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.v1.job_coach.coach.application.CoachService;
import org.v1.job_coach.coach.dao.CoachRepository;
import org.v1.job_coach.coach.domain.Coach;
import org.v1.job_coach.coach.dto.CoachDetailResponseDto;
import org.v1.job_coach.coach.dto.CoachResponseDto;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;

import java.util.List;

@Slf4j
@Service
public class CoachServiceImpl implements CoachService {

    private final CoachRepository coachRepository;

    public CoachServiceImpl(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    @Override
    public ResultResponseDto<?> getAllCoaches(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Coach> coaches = coachRepository.findAllBy(pageable);

        if (page >= coaches.getTotalPages()){
            throw new CustomException(Error.INVALID_PAGE);
        }


        log.info("[]");
        List<CoachResponseDto> list =
                coaches.stream()
                        .map(CoachResponseDto::toDto)
                        .toList();

        return ResultResponseDto.toDataResponseDto(200, "면접 코치 목록을 반환합니다.", list);

    }

    public ResultResponseDto<?> getCoachDetails(Long userId) {
        Coach coach = coachRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        CoachDetailResponseDto dto = CoachDetailResponseDto.toDto(coach);

        return ResultResponseDto.toDataResponseDto(200, "면접 상세 정보를 반환합니다.", dto);
    }

}
