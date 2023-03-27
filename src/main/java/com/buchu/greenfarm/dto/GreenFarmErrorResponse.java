package com.buchu.greenfarm.dto;

import com.buchu.greenfarm.exception.GreenFarmErrorCode;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class GreenFarmErrorResponse {
    private GreenFarmErrorCode greenFarmErrorCode;
    @Builder.Default
    private String errorName = "ERROR!";
    @Builder.Default
    private String detailMessage = "에러가 발생했습니다.";
}
