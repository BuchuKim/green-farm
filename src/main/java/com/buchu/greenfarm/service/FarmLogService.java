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
import com.buchu.greenfarm.repository.GoodRepository;
import com.buchu.greenfarm.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FarmLogService {
    private final FarmLogRepository farmLogRepository;
    private final UserRepository userRepository;
    private final GoodRepository goodRepository;
    private final HttpSession httpSession;

    @Transactional
    public Map<String, Object> getAllFarmLogsPage(Pageable pageable,
                                                  Boolean following) {
        PageImpl<FarmLog> farmLogs = following
                ? farmLogRepository.findFollowingFarmLogsQueryDslPaging(pageable, getSessionUser())
                : farmLogRepository.findAllFarmLogsQueryDslPaging(pageable);
        Map<String, Object> returnObj = new HashMap<>();
        returnObj.put("hasNext",farmLogs.hasNext());
        returnObj.put("farmLogs",farmLogs.stream().map(FarmLogDto::fromEntity).collect(Collectors.toList()));
        return returnObj;
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

    @Transactional
    public Page<FarmLog> searchFarmLog(final String keyword,
                              final Pageable pageable) {
        return farmLogRepository
                .findByKeyWordQueryDslPaging(keyword,pageable);
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
