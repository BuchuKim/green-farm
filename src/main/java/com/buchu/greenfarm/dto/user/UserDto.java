package com.buchu.greenfarm.dto.user;

import com.buchu.greenfarm.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String name;
    private LocalDateTime createdAt;
    private String bio;
    private int followerNum;
    private int followingNum;
    @Builder.Default
    private Boolean isFollowing = false;

    public static UserDto fromEntity(final User user,
                                     final int followingNum,
                                     final int followerNum,
                                     final Boolean isFollowing) {
        return UserDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .createdAt(user.getCreatedAt())
                .bio(user.getBio())
                .followingNum(followingNum)
                .followerNum(followerNum)
                .isFollowing(isFollowing)
                .build();
    }
}
