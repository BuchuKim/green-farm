package com.buchu.greenfarm.service;

import com.buchu.greenfarm.code.FarmLogStatusCode;
import com.buchu.greenfarm.config.auth.dto.SessionUser;
import com.buchu.greenfarm.dto.farmLog.CreateFarmLog;
import com.buchu.greenfarm.dto.farmLog.EditFarmLog;
import com.buchu.greenfarm.dto.farmLog.FarmLogDetailDto;
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
        return farmLogRepository
                .getByFarmLogStatusCodeOrderByCreatedAtDesc(
                        FarmLogStatusCode.PRESENTED).stream()
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
                        .getByAuthor(follow.getFollowed()))
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
    public FarmLogDetailDto getFarmLogDetail(Long id) {
        return FarmLogDetailDto.fromEntity(getFarmLogById(id));
    }

    @Transactional
    public Boolean checkIsLikedByCurrentUser(final Long id) {
        if (!isLoggedIn()) return false;
        return goodRepository.findByLikerAndFarmLog(
                getSessionUser(),
                getFarmLogById(id))
                .isPresent();
    }

    @Transactional
    public FarmLog getFarmLogById(Long id) {
        FarmLog foundFarmLog = farmLogRepository.findById(id)
                .orElseThrow(() -> new GreenFarmException(GreenFarmErrorCode.NO_FARM_LOG_ERROR));
        if (foundFarmLog.getFarmLogStatusCode().equals(FarmLogStatusCode.DELETED)) {
            throw new GreenFarmException(GreenFarmErrorCode.DELETED_FARM_LOG_ERROR);
        }
        return foundFarmLog;
    }

    @Transactional
    public Long getCreatedFarmLogId(CreateFarmLog.Request request) {
        return farmLogRepository.save(createFarmLogFromRequest(request)).getFarmLogId();
    }

    @Transactional
    public CreateFarmLog.Response createFarmLog(CreateFarmLog.Request request) {
        return CreateFarmLog.Response.fromEntity(
                farmLogRepository.save(createFarmLogFromRequest(request)));
    }

    @Transactional
    public FarmLogDetailDto editFarmLog(Long id, EditFarmLog.Request request) {
        return FarmLogDetailDto.fromEntity(
                getUpdatedFarmLog(getFarmLogById(id),request));
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
                        sessionUser, farmLog).isEmpty()) {
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

    public FarmLog getUpdatedFarmLog(FarmLog farmLog, EditFarmLog.Request request) {
        farmLog.setLogContent(request.getLogContent());
        return farmLog;
    }


    private FarmLog createFarmLogFromRequest(CreateFarmLog.Request request) {
        return FarmLog.builder()
                .logContent(request.getLogContent())
                .commentNum(0)
                .farmLogStatusCode(FarmLogStatusCode.PRESENTED)
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
        String email = currentUser.getEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new GreenFarmException(GreenFarmErrorCode.NO_USER_ERROR));
    }

}
