package com.buchu.greenfarm.entity;

import com.buchu.greenfarm.dto.user.UserProfileDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 10, message = "아이디는 4자 이상 10자 이하여야 합니다!")
    private String userId;

    @NotBlank
    @Size(min = 1, max = 15, message = "닉네임이 공란이거나 15자 이상이어선 안됩니다!")
    private String name; // nickname

    @Pattern(regexp="(^$|[0-9]{11})",message = "올바른 휴대전화 번호를 입력해주세요!")
    private String phoneNum;

    @Email
    @NotBlank
    private String email;

    // 자기소개
    @Size(max = 150, message = "자기소개 항목은 150자 이하의 길이여야 합니다.")
    private String bio;
    
    // OAuth2
    private String registrationId;
    private String oAuth2Id;

    @Enumerated(EnumType.STRING)
    private Role role;

    // 유저가 가지고 있는 farmLog
    @OneToMany(mappedBy = "author",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<FarmLog> farmLog;

    // 이 유저가 팔로우하고 있는 사람
    @OneToMany(mappedBy = "following",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Follow> following;

    // 이 유저가 팔로우 당하고 있는 사람.. (팔로워!!)
    @OneToMany(mappedBy = "followed",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Follow> followed;

    // 유저가 좋아요하는 farm 목록
    @OneToMany(mappedBy = "liker",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Good> likeList;

    public String getRoleKey() {
        return role.getKey();
    }

    public User editByDto(UserProfileDto userProfileDto) {
        this.userId = userProfileDto.getUserId();
        this.name = userProfileDto.getName();
        this.bio = userProfileDto.getBio();
        return this;
    }

    public int getFollowingNum() {
        return this.following.size();
    }
    public int getFollowerNum() {
        return this.followed.size();
    }
}
