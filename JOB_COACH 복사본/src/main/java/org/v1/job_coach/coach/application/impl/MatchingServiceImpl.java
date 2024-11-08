package org.v1.job_coach.coach.application.impl;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.v1.job_coach.coach.application.MatchingService;
import org.v1.job_coach.coach.domain.Coach;
import org.v1.job_coach.coach.dto.MatchCoachResponseDto;
import org.v1.job_coach.coach.dto.MatchUserResponseDto;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.user.domain.User;
import org.springframework.stereotype.Service;
import org.v1.job_coach.coach.dao.CoachRepository;
import org.v1.job_coach.coach.dao.MatchingRepository;
import org.v1.job_coach.coach.domain.Matching;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.user.dao.UserRepository;

import java.util.List;

@Service
public class MatchingServiceImpl implements MatchingService {

    private final UserRepository userRepository;
    private final CoachRepository coachRepository;
    private final MatchingRepository matchingRepository;

    public MatchingServiceImpl(UserRepository userRepository, CoachRepository coachRepository, MatchingRepository matchingRepository) {
        this.userRepository = userRepository;
        this.coachRepository = coachRepository;
        this.matchingRepository = matchingRepository;
    }

    /*
    * 구현해야 할 기능
    * 1. 매칭 요청
    * match(userId, coachId)
    * user = userId.findById -> User 있는지 검사
    * coach = coachId.findById -> Coach 있는지 검사
    * Matching matching = new Matching(user, coach) -> 사용자 요청 상태
    *
    * 2. 매칭 수락
    * acceptMatch(matchingId, coachId)
    * Matching matching = matchingRepository.findById(matchingId);
    * Coach coach = coachRepository.findById(coachId)
    * matching.accept(coach)
    *
    * 3. 매칭 거절
    * rejectMatch(matchingId, coachId)
     * Matching matching = matchingRepository.findById(matchingId);
     * Coach coach = coachRepository.findById(coachId)
     * matching.reject(coach)
     *
     *4. 매칭 취소
     * cancelMatch(matchingId, coachId)
     * Matching matching = matchingRepository.findById(matchingId);
     * Coach coach = coachRepository.findById(coachId)
     * matching.cancel(coach)
     *
     * 5. 매칭 완료
     * completeMatch(matchingId, coachId)
     * Matching matching = matchingRepository.findById(matchingId);
     * Coach coach = coachRepository.findById(coachId)
     * matching.complete(coach)
     *
     * 6. 매칭 된 코치 보여주기
     * List<Matching> getUserMatchings(User user)
     * return matchingRepository.findByUser(user);
     *
     * * 7. 매칭 된 사용자 보여주기
     * List<Matching> getCoachMatchings(Coach coach)
     * return matchingRepository.findByCoach(coach);
    * */

    @Transactional
    public ResultResponseDto<?> match(User user, Long coachId) {
        Coach coach = validateUserAndGetCoach(user, coachId);
        Matching matching = new Matching(user, coach);
        matchingRepository.save(matching);
        return ResultResponseDto.toResultResponseDto(200, "매칭 요청이 정상적으로 처리되었습니다.");
    }

    @Transactional
    public ResultResponseDto<?> acceptMatch(User user, Long matchingId) {
        Matching matching = checkMatchingAndAuthorization(user, matchingId);
        matching.accept();
        return ResultResponseDto.toResultResponseDto(200, "매칭을 성공적으로 수락하였습니다.");
    }

    @Transactional
    public ResultResponseDto<?> rejectMatch(User user, Long matchingId) {
        Matching matching = checkMatchingAndAuthorization(user, matchingId);
        matching.reject();
        return ResultResponseDto.toResultResponseDto(200, "매칭을 성공적으로 거절하였습니다.");
    }

    @Transactional
    public ResultResponseDto<?> cancelMatch(User user, Long matchingId) {
        Matching matching = validateUserAndGetMatching(user, matchingId);
        if (!matching.getUser().equals(user)) {
            throw new CustomException(Error.NOT_AUTHORIZED);}
        matching.cancel();
        return ResultResponseDto.toResultResponseDto(200, "매칭을 성공적으로 취소하였습니다.");
    }

    @Transactional
    public ResultResponseDto<?> completeMatch(User user, Long matchingId) {
        Matching matching = checkMatchingAndAuthorization(user, matchingId);
        matching.complete();
        return ResultResponseDto.toResultResponseDto(200, "매칭을 성공적으로 거절하였습니다.");
    }


    public ResultResponseDto<?> userMatchingList(User user, int page) {
        findUserById(user.getPid());
        Page<Matching> byUser = matchingRepository.findByUser(user, PageRequest.of(page, 5));
        List<MatchUserResponseDto> data = byUser.stream().map(MatchUserResponseDto::toDto).toList();
        return ResultResponseDto.toDataResponseDto(200, "매칭중인 면접 코치 리스트를 반환합니다.", data);
    }

    public ResultResponseDto<?> coachMatchingList(User user, int page) {
        Coach coach = authorizedCoach(user);
        findCoachById(coach.getPid());

        Page<Matching> byCoach = matchingRepository.findByCoach(coach, PageRequest.of(page, 5));
        List<MatchCoachResponseDto> data = byCoach.stream().map(MatchCoachResponseDto::toDto).toList();
        return ResultResponseDto.toDataResponseDto(200, "매칭중인 면접 코치 리스트를 반환합니다.", data);
    }

    private void findUserById(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
    }
    private Coach findCoachById(Long coachId) {
        return coachRepository.findById(coachId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_COACH));
    }
    private Coach validateUserAndGetCoach(User user, Long coachId) {
        findUserById(user.getPid());
        return findCoachById(coachId);
    }

    private Matching validateUserAndGetMatching(User user, Long matchingID) {
        findUserById(user.getPid());
        return findMatchingById(matchingID);
    }

    private Matching findMatchingById(Long matchingId) {
        return matchingRepository.findById(matchingId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_MATCH));
    }

    /*로그인 한 사람이 Coach 권한을 가지고있는지 확인*/
    private Coach authorizedCoach(User user) {
        if (!user.isCoach()) {
            throw new CustomException(Error.NOT_AUTHORIZED);}
        return (Coach) user;
    }

    /* 코치가 매칭 권한이 있는지 확인 */
    private Matching checkMatchingAndAuthorization(User user, Long matchingId) {
        Coach coach = authorizedCoach(user);

        Matching matching = findMatchingById(matchingId);
        if (!matching.getCoach().equals(coach)) {
            throw new CustomException(Error.NOT_AUTHORIZED);
        }
        return matching;
    }


}
