package com.buchu.greenfarm.dto.farmLog;

import com.buchu.greenfarm.entity.FarmLog;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FarmLogDto {
    // home(index)에서 보이는 farm log

    private String logContent;
    private int likeNum;
    private Long farmLogId;
    private String authorName;
    private String authorId;
    private String createdAt;

    public static FarmLogDto fromEntity(FarmLog farmLog) {
        return FarmLogDto.builder()
                .logContent(farmLog.getLogContent())
                .farmLogId(farmLog.getFarmLogId())
                .authorId(farmLog.getAuthor().getUserId())
                .authorName(farmLog.getAuthor().getName())
                .createdAt(createdAtString(farmLog.getCreatedAt()))
                .likeNum(farmLog.getLikeNum())
                .build();
    }

    private static String createdAtString(LocalDateTime createdAt) {
        return DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm").format(createdAt);
    }
}
