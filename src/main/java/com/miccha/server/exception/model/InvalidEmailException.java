package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidEmailException extends MicchaException {
    public InvalidEmailException() {
        super(ErrorCode.INVALID_EMAIL, HttpStatus.BAD_REQUEST);
    }
}
