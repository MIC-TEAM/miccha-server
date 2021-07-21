package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class PasswordUpdateFailedException extends MicchaException {
    public PasswordUpdateFailedException() {
        super(ErrorCode.PASSWORD_UPDATE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
