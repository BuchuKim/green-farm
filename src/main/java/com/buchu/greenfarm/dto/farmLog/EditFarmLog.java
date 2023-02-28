package com.buchu.greenfarm.dto.farmLog;

import lombok.*;

public class EditFarmLog {
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        private String logContent;

    }
}
