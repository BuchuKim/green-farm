package com.buchu.greenfarm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Good extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_log")
    private FarmLog farmLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liker")
    private User liker;
}
