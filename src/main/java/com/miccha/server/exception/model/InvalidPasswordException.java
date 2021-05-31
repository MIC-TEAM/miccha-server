package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends MicchaException {
    public InvalidPasswordException() {
        super(ErrorCode.INVALID_PASSWORD, HttpStatus.BAD_REQUEST);
    }
}
