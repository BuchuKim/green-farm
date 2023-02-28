package com.buchu.greenfarm.dto.farmLog;

import com.buchu.greenfarm.code.FarmLogStatusCode;
import com.buchu.greenfarm.entity.FarmLog;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

public class CreateFarmLog {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Request {
        @NotBlank
        @Size(max = 300, message = "농장 일기는 0자 이상 300자 이하여야 합니다!")
        private String logContent;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        @NotBlank
        private String logContent;

        @NonNull
        private String authorId;

        @NonNull
        private String authorName;

        @NonNull
        private FarmLogStatusCode farmLogStatusCode;

        public static CreateFarmLog.Response fromEntity(@NonNull FarmLog farmLog) {
            return Response.builder()
                    .logContent(farmLog.getLogContent())
                    .authorId(farmLog.getAuthor().getUserId())
                    .authorName(farmLog.getAuthor().getName())
                    .farmLogStatusCode(farmLog.getFarmLogStatusCode())
                    .build();
        }
    }
}
