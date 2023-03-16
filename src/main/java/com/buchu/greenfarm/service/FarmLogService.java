package com.buchu.greenfarm.service;

import com.buchu.greenfarm.config.auth.dto.SessionUser;
import com.buchu.greenfarm.dto.farmLog.CreateFarmLog;
import com.buchu.greenfarm.dto.farmLog.FarmLogDto;
import com.buchu.greenfarm.entity.FarmLog;
import com.buchu.greenfarm.entity.Good;
import com.buchu.greenfarm.entity.User;
import com.buchu.greenfarm.exception.GreenFarmErrorCode;
import com.buchu.greenfarm.exception.GreenFarmException;
import com.buchu.greenfarm.repository.FarmLogRepository;
import com.buchu.greenfarm.repository.FollowRepository;
import com.buchu.greenfarm.repository.GoodRepository;
import com.buchu.greenfarm.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FarmLogService {
    private final FarmLogRepository farmLogRepository;
    private final UserRepository userRepository;
    private final GoodRepository goodRepository;
    private final FollowRepository followRepository;
    private final HttpSession httpSession;

    @Transactional
    public List<FarmLogDto> getAllFarmLogs() {
        // 모든 일기들을 시간 순으로
        return farmLogRepository
                .findAllForIndex().stream()
                .map(FarmLogDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional public List<FarmLogDto> getFollowingFarmLogs() {
        List<FarmLogDto> farmLogs = new ArrayList<>();

        followRepository.findByFollowing(
                        // 1. following 목록의 user 불러옴
                        getSessionUser()).stream()
                // 2. following 유저의 farmLog 불러옴
                .map(follow -> farmLogRepository
                        .findByAuthor(follow.getFollowed()))
                // 3. DTO 변환
                .map(logs -> logs.stream()
                        .map(FarmLogDto::fromEntity))
                // 4. 결과 합침
                .forEach(farmLogDtos -> farmLogDtos
                        .forEach(farmLogs::add));

        // 5. 최신 순으로 정렬 후 Return
        farmLogs.sort(Comparator.comparing(FarmLogDto::getCreatedAt));

        return farmLogs;
    }

    @Transactional
    public FarmLogDto getFarmLogDetail(Long id) {
        FarmLog foundFarmLog = getFarmLogById(id);
        return FarmLogDto.fromEntity(foundFarmLog)
                .setIsLikedByCurrentUser(
                        checkIsLikedByCurrentUser(foundFarmLog));
    }

    @Transactional
    public Boolean checkIsLikedByCurrentUser(final FarmLog farmLog) {
        if (!isLoggedIn()) return false;
        return goodRepository.findByLikerAndFarmLog(
                getSessionUser(),
                farmLog)
                .isPresent();
    }

    @Transactional
    public FarmLog getFarmLogById(Long id) {
        return farmLogRepository.findById(id)
                .orElseThrow(() -> new GreenFarmException(
                        GreenFarmErrorCode.NO_FARM_LOG_ERROR));
    }

    @Transactional
    public Long getCreatedFarmLogId(CreateFarmLog.Request request) {
        return farmLogRepository.save(
                FarmLog.builder()
                    .logContent(request.getLogContent())
                    .author(getSessionUser())
                    .build())
                .getFarmLogId();
    }

    @Transactional
    public void deleteFarmLog(final Long farmLogId) {
        FarmLog beingDeletedFarmLog = getFarmLogById(farmLogId);
        validateCurrentUser(beingDeletedFarmLog.getAuthor().getUserId());
        farmLogRepository.delete(
                beingDeletedFarmLog
        );
    }

    @Transactional
    public void likeFarmLog(final Long farmLogId) {
        User sessionUser = getSessionUser();
        FarmLog farmLog = getFarmLogById(farmLogId);
        if (goodRepository.findByLikerAndFarmLog(
                        sessionUser, farmLog)
                .isEmpty()) {
            goodRepository.save(
                    Good.builder()
                            .liker(sessionUser)
                            .farmLog(farmLog)
                            .build());
        }
    }

    @Transactional
    public void unlikeFarmLog(final Long farmLogId) {
        goodRepository.findByLikerAndFarmLog(
                getSessionUser(), getFarmLogById(farmLogId))
                .ifPresent(goodRepository::delete);
    }

    private FarmLog createFarmLogFromRequest(CreateFarmLog.Request request) {
        return FarmLog.builder()
                .logContent(request.getLogContent())
                .author(getSessionUser())
                .build();
    }

    private Boolean isLoggedIn() {
        return httpSession.getAttribute("user")!=null;
    }

    @Transactional
    private void validateCurrentUser(
            final String userId) {
        if (!getSessionUser().getUserId().equals(userId)) {
            throw new GreenFarmException(
                    GreenFarmErrorCode.INVALID_REQUEST_USER);
        }
    }

    @Transactional
    private User getSessionUser() {
        SessionUser currentUser = (SessionUser) httpSession.getAttribute("user");
        if (currentUser==null) {
            throw new GreenFarmException(GreenFarmErrorCode.NEED_LOGIN);
        }
        return userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new GreenFarmException(GreenFarmErrorCode.NO_USER_ERROR));
    }

}
