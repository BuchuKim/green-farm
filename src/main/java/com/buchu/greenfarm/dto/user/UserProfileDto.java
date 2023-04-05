package com.buchu.greenfarm.dto.user;

import com.buchu.greenfarm.entity.User;
import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    @NotBlank(message = "ID가 공란이어선 안됩니다!")
    @Pattern(regexp = "^[a-zA-Z0-9]*$",message = "아이디는 숫자와 알파벳으로만 이루어져야 합니다!")
    @Size(min=4,max = 15,message = "ID는 4자 이상 15자 이하의 길이여야 합니다!")
    private String userId;

    @NotBlank(message = "닉네임이 공란이어선 안됩니다!")
    @Pattern(regexp = "^[a-zA-Z가-힣][a-zA-Z가-힣0-9]{0,9}$",
            message = " 숫자, 한글, 알파벳으로만 이루어져야 합니다!")
    @Size(min = 1, max = 10,
            message = "닉네임은 10자 이하의 길이여야 합니다!")
    private String name;

    @NotBlank(message = "이메일이 공란이어선 안됩니다!")
    @Email(message = "유효한 이메일을 입력해주세요.")
    private String email;

    @Size(max = 150,
            message = "자기소개는 150자 이하의 길이여야 합니다!")
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
