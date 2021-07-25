package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends MicchaException {
    public InvalidRefreshTokenException() {
        super(ErrorCode.INVALID_REFRESH_TOKEN, HttpStatus.BAD_REQUEST);
    }
}
