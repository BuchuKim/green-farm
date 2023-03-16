package com.buchu.greenfarm.exception;

import lombok.Getter;

@Getter
public class GreenFarmException extends RuntimeException {
    private final GreenFarmErrorCode greenFarmErrorCode;
    private final String detailMessage;

    public GreenFarmException(GreenFarmErrorCode greenFarmErrorCode) {
        super(greenFarmErrorCode.getDetailMessage());
        this.greenFarmErrorCode = greenFarmErrorCode;
        this.detailMessage = greenFarmErrorCode.getDetailMessage();
    }
}
