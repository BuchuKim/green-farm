package com.buchu.greenfarm.entity;

import com.buchu.greenfarm.code.NotificationCode;
import com.buchu.greenfarm.exception.GreenFarmErrorCode;
import com.buchu.greenfarm.exception.GreenFarmException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Notification extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne @JoinColumn(name = "sending_user")
    private User sendingUser;

    @ManyToOne @JoinColumn(name = "receiving_user")
    private User receivingUser;

    @OneToOne @JoinColumn(name = "farm_log")
    private FarmLog farmLog;

    private String message;

    @NotNull
    private NotificationCode notificationCode;

    private Boolean isRead;

    public String getMessage() {
        switch (notificationCode) {
            case TAG_ALARM -> {
                return this.sendingUser.getName()
                        + "님이 "
                        + this.receivingUser.getName()
                        + "님을 태그하셨습니다.";
            }
            case LIKE_ALARM -> {
                return this.sendingUser.getName()
                        + "님이 "
                        + this.receivingUser.getName()
                        + "님의 일기에 좋아요를 누르셨습니다.";
            }
            case FOLLOW_ALARM -> {
                return this.sendingUser.getName()
                        + "님이 "
                        + this.receivingUser.getName()
                        + "님을 팔로우합니다.";
            }
            default -> {
                throw new GreenFarmException(GreenFarmErrorCode.UNKNOWN_ALARM_ERROR);
            }
        }
    }
}
