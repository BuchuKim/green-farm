package com.buchu.greenfarm.dto.farmLog;

import com.buchu.greenfarm.entity.FarmLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Slf4j
public class FarmLogDto {

    private String logContent;
    private int likeNum;
    private Long farmLogId;
    private String authorName;
    private String authorId;
    private String createdAt;
    private Boolean isLikedByCurrentUser;

    public static FarmLogDto fromEntity(final FarmLog farmLog) {
        return FarmLogDto.builder()
                .logContent(farmLog.getLogContent())
                .farmLogId(farmLog.getFarmLogId())
                .authorId(farmLog.getAuthor().getUserId())
                .authorName(farmLog.getAuthor().getName())
                .createdAt(createdAtString(farmLog.getCreatedAt()))
                .likeNum(farmLog.getLikeNum())
                .build();
    }

    public FarmLogDto setIsLikedByCurrentUser(Boolean isLikedByCurrentUser) {
        this.isLikedByCurrentUser = isLikedByCurrentUser;
        return this;
    }

    private static String createdAtString(LocalDateTime createdAt) {
        return DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
                .withZone(ZoneId.of("Asia/Seoul")).format(
                        createdAt.atZone(ZoneId.of("UTC")).toInstant());
    }
}
