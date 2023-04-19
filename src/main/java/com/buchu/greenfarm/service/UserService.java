package com.buchu.greenfarm.service;

import com.buchu.greenfarm.config.auth.dto.SessionUser;
import com.buchu.greenfarm.dto.farmLog.FarmLogDto;
import com.buchu.greenfarm.dto.user.DeleteUserDto;
import com.buchu.greenfarm.dto.user.UserDetailDto;
import com.buchu.greenfarm.dto.user.UserDto;
import com.buchu.greenfarm.dto.user.UserProfileDto;
import com.buchu.greenfarm.entity.FarmLog;
import com.buchu.greenfarm.entity.Follow;
import com.buchu.greenfarm.entity.User;
import com.buchu.greenfarm.exception.GreenFarmErrorCode;
import com.buchu.greenfarm.exception.GreenFarmException;
import com.buchu.greenfarm.repository.FarmLogRepository;
import com.buchu.greenfarm.repository.FollowRepository;
import com.buchu.greenfarm.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final HttpSession httpSession;
    private final UserRepository userRepository;
    private final FarmLogRepository farmLogRepository;
    private final FollowRepository followRepository;
    private final NotificationService notificationService;

    @Transactional
    public UserDto getUserDetail(final User currentPageUser) {
        return UserDto.fromEntity(currentPageUser,
                followRepository.countByFollowing(currentPageUser),
                followRepository.countByFollowed(currentPageUser),
                getSessionUser() != null
                        && followRepository.findByFollowingAndFollowed(
                                getUserByUserId(getSessionUser().getUserId()),
                                currentPageUser)
                        .isPresent());
    }

    @Transactional
    public Map<String, Object> getFarmLogPagesOfCurrentPageUser(
            final User currentPageUser,
            final Boolean isLike,
            final Pageable pageable) {
        PageImpl<FarmLog> farmLogs = isLike
                ? farmLogRepository.findByLikerQueryDslPaging(
                        currentPageUser, pageable)
                : farmLogRepository.findByAuthorQueryDslPaging(
                        currentPageUser, pageable);

        Map<String, Object> data = new HashMap<>();
        data.put("hasNext",farmLogs.hasNext());
        data.put("farmLogs",
                farmLogs.stream()
                .map(FarmLogDto::fromEntity)
                .collect(Collectors.toList()));

        return data;
    }

    @Transactional
    public UserProfileDto getUserProfileDto(final String userId) {
        return UserProfileDto
                .fromEntity(getUserByUserId(userId));
    }

    @Transactional
    public void editUser(final String userId,
                         final UserProfileDto userProfileDto) {
        validateCurrentUser(userProfileDto.getEmail());
        validateUserId(userProfileDto.getUserId());
        httpSession.setAttribute(
                "user",new SessionUser(
                        getUserByUserId(userId)
                                .editByDto(userProfileDto)));
    }

    private void validateCurrentUser(String beingEditedUserEmail) {
        // validate if current user is equal to currently edited user
        if (!getSessionUser().getEmail()
                .equals(beingEditedUserEmail)) {
            throw new GreenFarmException(
                    GreenFarmErrorCode.INVALID_REQUEST_USER);
        }
    }

    @Transactional
    public void follow(final String followingId,
                           final String followedId) {
        User followed = getUserByUserId(followedId);
        User following = getUserByUserId(followingId);
        if (followRepository.findByFollowingAndFollowed(
                following,followed).isEmpty()) {
            notificationService.sendFollowNotification(following, followed);
            followRepository.save(
                    Follow.builder()
                            .following(following)
                            .followed(followed)
                            .build());
        }
    }

    @Transactional
    public void unfollow(final String followingId,
                         final String followedId) {
        followRepository.findByFollowingAndFollowed(
                            getUserByUserId(followingId),
                            getUserByUserId(followedId))
                .ifPresent(followRepository::delete);
    }

    @Transactional
    public List<UserProfileDto> getFollowingUserDto(final String userId) {
        return followRepository.findByFollowing(
                getUserByUserId(userId))
                .stream().map(Follow::getFollowed)
                .map(UserProfileDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserProfileDto> getFollowerUserDto(final String userId) {
        return followRepository.findByFollowed(
                        getUserByUserId(userId))
                .stream().map(Follow::getFollowing)
                .map(UserProfileDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new GreenFarmException(
                        GreenFarmErrorCode.NO_USER_ERROR));
    }

    @Transactional
    private void validateUserId(String userId) {
        if (getSessionUser()
                .getUserId().equals(userId)) {
            // no validation if id is not edited
            return;
        }

        // validate if modified userID is already in DB
        userRepository.findByUserId(userId)
                .ifPresent(user -> {
                    throw new GreenFarmException(
                            GreenFarmErrorCode.DUPLICATED_USER_ID);
                });
    }

    @Transactional
    public DeleteUserDto getBeingDeletedUser() {
        return DeleteUserDto.fromEntity(
                getUserByUserId(getSessionUser().getUserId()));
    }

    @Transactional
    public void deleteUser(final String userId) {
        validateDeletedUser(userId);
        userRepository.delete(getUserByUserId(userId));
    }

    @Transactional
    private void validateDeletedUser(final String beingDeletedUserId) {
        if (!getSessionUser().getUserId().equals(beingDeletedUserId)) {
            throw new GreenFarmException(GreenFarmErrorCode.INVALID_REQUEST_USER);
        }
    }

    private SessionUser getSessionUser() {
        return (SessionUser) httpSession.getAttribute("user");
    }
}
