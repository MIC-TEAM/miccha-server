package com.miccha.server;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ErrorCode {
    SUCCESS(0),
    DUPLICATE_EMAIL(1),
    INVALID_EMAIL(2),
    INVALID_PASSWORD(3),
    MISSING_EMAIL(4);

    @Getter
    private int code;

    public boolean isSuccess() {
        return this == SUCCESS;
    }
}
