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
    DUPLICATE_EMAIL(7),
    INVALID_MOVIE_ID(8),
    SEARCH_QUERY_NOT_FOUND(9),
    EMPTY_REQUEST_BODY(10),
    EMAIL_CHANGE_TOKEN_NOT_FOUND(11),
    EMAIL_NOT_FOUND(12),
    NAME_UPDATE_FAILED(13),
    PASSWORD_UPDATE_FAILED(14),
    EMAIL_CHANGE_TOKEN_UPDATE_FAILED(15),
    EMAIL_UPDATE_FAILED(16);

    @Getter
    private int code;
}
