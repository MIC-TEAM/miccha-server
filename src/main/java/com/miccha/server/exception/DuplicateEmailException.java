package com.miccha.server.exception;

import com.miccha.server.ErrorCode;

public class DuplicateEmailException extends MicchaException {
    @Override
    public int getErrorCode() {
        return ErrorCode.DUPLICATE_EMAIL.getCode();
    }
}
