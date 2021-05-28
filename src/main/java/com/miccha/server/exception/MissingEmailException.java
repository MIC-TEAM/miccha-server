package com.miccha.server.exception;

import com.miccha.server.ErrorCode;

public class MissingEmailException extends MicchaException {
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.MISSING_EMAIL;
    }
}
