package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidNameException extends MicchaException {
    public InvalidNameException() {
        super(ErrorCode.INVALID_NAME, HttpStatus.BAD_REQUEST);
    }
}
