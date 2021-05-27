package com.miccha.server.exception;

import com.miccha.server.ErrorCode;

public class InvalidEmailException extends MicchaException {
    @Override
    public int getErrorCode() {
        return ErrorCode.INVALID_EMAIL.getCode();
    }
}
