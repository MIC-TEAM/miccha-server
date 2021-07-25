package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class RefreshTokenNotFoundException extends MicchaException {
    public RefreshTokenNotFoundException() {
        super(ErrorCode.REFRESH_TOKEN_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }
}
