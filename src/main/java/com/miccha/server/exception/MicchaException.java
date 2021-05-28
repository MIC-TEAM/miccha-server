package com.miccha.server.exception;

import com.miccha.server.ErrorCode;

public abstract class MicchaException extends RuntimeException {
    public abstract ErrorCode getErrorCode();
}
