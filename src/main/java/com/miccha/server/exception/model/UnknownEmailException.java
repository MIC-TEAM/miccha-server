package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class UnknownEmailException extends MicchaException {
    public UnknownEmailException() {
        super(ErrorCode.UNKNOWN_EMAIL, HttpStatus.BAD_REQUEST);
    }
}
