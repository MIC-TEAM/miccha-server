package com.miccha.server.exception;

import com.miccha.server.ErrorCode;

public class InvalidPasswordException extends MicchaException {
    @Override
    public int getErrorCode() {
        return ErrorCode.INVALID_PASSWORD.getCode();
    }
}
