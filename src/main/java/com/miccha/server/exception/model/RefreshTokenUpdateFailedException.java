package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class RefreshTokenUpdateFailedException extends MicchaException {
    public RefreshTokenUpdateFailedException() {
        super(ErrorCode.REFRESH_TOKEN_UPDATE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
