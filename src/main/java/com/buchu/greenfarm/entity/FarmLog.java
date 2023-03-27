package com.buchu.greenfarm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class FarmLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long farmLogId;

    @NotBlank(message = "내용이 공란이어선 안됩니다.")
    @Size(max = 300, message = "농장 일기는 300자를 넘겨선 안됩니다!")
    private String logContent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author", updatable = false)
    private User author;

    // 좋아요 한 유저 목록
    @OneToMany(mappedBy = "farmLog",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Good> likers;

    public int getLikeNum() {
        return this.likers.size();
    }
}
