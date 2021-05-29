package com.miccha.server.exception;

import com.miccha.server.ErrorCode;

public class RequestMissingEmailException extends MicchaException {
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.REQUEST_MISSING_EMAIL;
    }
}
