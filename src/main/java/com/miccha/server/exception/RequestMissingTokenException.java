package com.miccha.server.exception;

import com.miccha.server.ErrorCode;

public class RequestMissingTokenException extends MicchaException {
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.REQUEST_MISSING_TOKEN;
    }
}
