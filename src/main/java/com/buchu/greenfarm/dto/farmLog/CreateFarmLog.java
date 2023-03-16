package com.buchu.greenfarm.dto.farmLog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

public class CreateFarmLog {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank
        @Size(max = 300, message = "농장 일기는 0자 이상 300자 이하여야 합니다!")
        private String logContent;
    }
}
