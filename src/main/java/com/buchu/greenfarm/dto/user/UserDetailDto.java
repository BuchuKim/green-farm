package com.buchu.greenfarm.dto.user;


import com.buchu.greenfarm.dto.farmLog.FarmLogDto;
import com.buchu.greenfarm.entity.FarmLog;
import com.buchu.greenfarm.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDto {
    private String userId;
    private String name;
    private String createdAt;
    private String bio;
    private int followerNum;
    private int followingNum;
    private Boolean isFollowing = false;
    private List<FarmLogDto> farmLogs;

    public static UserDetailDto fromEntity(User user, List<FarmLog> farmLogs) {
        return UserDetailDto.builder()
                .userId(user.getUserId())
                .bio(user.getBio())
                .createdAt(createdAtString(user.getCreatedAt()))
                .name(user.getName())
                .farmLogs(farmLogs.stream().map(FarmLogDto::fromEntity).collect(Collectors.toList()))
                .build();
    }

    public UserDetailDto setIsFollowing(Boolean isFollowing) {
        this.isFollowing = isFollowing;
        return this;
    }

    public UserDetailDto setFollowNums(int followingNum, int followerNum) {
        this.followingNum = followingNum;
        this.followerNum = followerNum;
        return this;
    }

    private static String createdAtString(LocalDateTime createdAt) {
        return DateTimeFormatter.ofPattern("yyyy.MM").format(createdAt);
    }
}
