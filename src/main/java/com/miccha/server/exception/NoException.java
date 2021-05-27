package com.miccha.server.exception;

import com.miccha.server.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoException extends MicchaException {
    public static NoException INSTANCE = new NoException();

    @Override
    public int getErrorCode() {
        return ErrorCode.SUCCESS.getCode();
    }
}
