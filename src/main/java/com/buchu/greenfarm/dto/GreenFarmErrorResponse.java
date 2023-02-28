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
    private String errorName;
    private String detailMessage;
}
