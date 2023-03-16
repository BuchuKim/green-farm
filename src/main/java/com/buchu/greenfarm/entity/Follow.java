package com.buchu.greenfarm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Follow extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @BatchSize(size = 1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following")
    private User following;

    @BatchSize(size = 1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed")
    private User followed;
}
