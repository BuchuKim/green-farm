package com.buchu.greenfarm.dto.user;

import com.buchu.greenfarm.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private String userId;
    private String name;
    private String email;
    private String bio;

    public static UserProfileDto fromEntity(User user) {
        return UserProfileDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .bio(user.getBio())
                .build();
    }
}
