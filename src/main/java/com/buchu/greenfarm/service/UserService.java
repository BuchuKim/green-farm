package com.buchu.greenfarm.service;

import com.buchu.greenfarm.config.auth.dto.SessionUser;
import com.buchu.greenfarm.dto.user.DeleteUserDto;
import com.buchu.greenfarm.dto.user.UserDetailDto;
import com.buchu.greenfarm.dto.user.UserProfileDto;
import com.buchu.greenfarm.entity.FarmLog;
import com.buchu.greenfarm.entity.Follow;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final HttpSession httpSession;
    private final UserRepository userRepository;
    private final FarmLogRepository farmLogRepository;
    private final FollowRepository followRepository;
    private final GoodRepository goodRepository;

    @Transactional
    public UserDetailDto getUserDetailDto(final String userId,
                                          final Boolean isLike) {
        User foundUser = userRepository.findByUserId(userId)
                .orElseThrow(()-> new GreenFarmException(
                        GreenFarmErrorCode.NO_USER_ERROR));
        List<FarmLog> foundFarmLogs = isLike ?
                goodRepository.findByLikerOrderByCreatedAtDesc(foundUser).stream()
                        .map(Good::getFarmLog)
                        .collect(Collectors.toList()) :
                farmLogRepository.getByAuthorOrderByCreatedAtDesc(foundUser);
        return UserDetailDto.fromEntity(foundUser,foundFarmLogs);
    }

    @Transactional
    public UserProfileDto getUserProfileDto(final String userId) {
        return UserProfileDto.fromEntity(getUserByUserId(userId));
    }

    @Transactional
    public void editUser(final String userId,
                         final UserProfileDto userProfileDto) {
        validateCurrentUser(userId);
        validateUserId(userProfileDto.getUserId());
        httpSession.setAttribute(
                "user",new SessionUser(
                        getUserByUserId(userId)
                                .editByDto(userProfileDto)));
    }

    private void validateCurrentUser(String beingEditedUserId) {
        if (!getSessionUser().getUserId()
                .equals(beingEditedUserId)) {
            throw new GreenFarmException(
                    GreenFarmErrorCode.INVALID_REQUEST_USER);
        }
    }

    @Transactional
    public void follow(final String followingId,
                           final String followedId) {
        if (followedId.equals(followingId)) {
            throw new GreenFarmException(
                    GreenFarmErrorCode.CANNOT_FOLLOW);
        }
        User followed = getUserByUserId(followedId);
        User following = getUserByUserId(followingId);
        validateFollow(following, followed);
        followRepository.save(
                Follow.builder()
                        .following(following)
                        .followed(followed)
                        .build()
        );
    }

    @Transactional
    public void unfollow(final String followingId,
                         final String followedId) {
        User following = getUserByUserId(followingId);
        User followed = getUserByUserId(followedId);
        followRepository.findByFollowingAndFollowed(
                following, followed)
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
    protected User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() ->new GreenFarmException(
                        GreenFarmErrorCode.NO_USER_ERROR));
    }

    @Transactional
    private void validateUserId(String userId) {
        if (getSessionUser()
                .getUserId().equals(userId)) {
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
    public Boolean isFollowing(final String followingId,
                               final String followedId) {
        return followRepository.findByFollowingAndFollowed(
                getUserByUserId(followingId),
                getUserByUserId(followedId))
                .isPresent();
    }

    @Transactional
    public void validateFollow(User following, User followed) {
        if (following.getUserId().equals(followed.getUserId())) {
            throw new GreenFarmException(GreenFarmErrorCode.CANNOT_FOLLOW);
        }
        followRepository.findByFollowingAndFollowed(
                following, followed)
                .ifPresent(follow -> {
                    throw new GreenFarmException(GreenFarmErrorCode.CANNOT_FOLLOW);
                });
    }

    @Transactional
    public DeleteUserDto getBeingDeletedUser() {
        return DeleteUserDto.fromEntity(
                getUserByUserId(getSessionUser().getUserId()));
    }

    @Transactional
    public void deleteUser(final String userId) {
        validateCurrentUser(userId);
        userRepository.delete(getUserByUserId(userId));
    }

    private SessionUser getSessionUser() {
        return (SessionUser) httpSession.getAttribute("user");
    }
}
