package com.miccha.server;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ErrorCode {
    SUCCESS(0),
    UNKNOWN_ERROR(1),
    REQUEST_MISSING_TOKEN(2),
    REQUEST_MISSING_PASSWORD(3),
    REQUEST_MISSING_EMAIL(4),
    INVALID_EMAIL(5),
    INVALID_PASSWORD(6),
    DUPLICATE_EMAIL(7);

    @Getter
    private int code;
}
