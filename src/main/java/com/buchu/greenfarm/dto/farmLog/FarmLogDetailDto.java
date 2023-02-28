package com.buchu.greenfarm.dto.farmLog;

import com.buchu.greenfarm.code.FarmLogStatusCode;
import com.buchu.greenfarm.entity.FarmLog;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FarmLogDetailDto {
    private Long farmLogId;
    private String logContent;
    private String authorId;
    private String authorName;
    private int likeNum;
    private int commentNum;
    private String createdAt;
    private FarmLogStatusCode farmLogStatusCode;

    public static FarmLogDetailDto fromEntity(FarmLog farmLog) {
        return FarmLogDetailDto.builder()
                .logContent(farmLog.getLogContent())
                .authorId(farmLog.getAuthor().getUserId())
                .authorName(farmLog.getAuthor().getName())
                .farmLogStatusCode(farmLog.getFarmLogStatusCode())
                .likeNum(farmLog.getLikeNum())
                .commentNum(farmLog.getCommentNum())
                .createdAt(createdAtString(farmLog.getCreatedAt()))
                .farmLogId(farmLog.getFarmLogId())
                .build();
    }

    private static String createdAtString(LocalDateTime createdAt) {
        return DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm").format(createdAt);
    }
}
