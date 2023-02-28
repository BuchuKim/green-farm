package com.buchu.greenfarm.dto.user;


import com.buchu.greenfarm.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserDto {
    private String name;
    private String email;
    private String userId;
    private String registrationId;
    private LocalDateTime createdAt;

    public static DeleteUserDto fromEntity(User user) {
        return DeleteUserDto.builder()
                .createdAt(user.getCreatedAt())
                .email(user.getEmail())
                .name(user.getName())
                .userId(user.getUserId())
                .registrationId(user.getRegistrationId())
                .build();
    }
}
