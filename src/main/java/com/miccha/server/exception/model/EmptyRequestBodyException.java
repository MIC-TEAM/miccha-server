package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class EmptyRequestBodyException extends MicchaException {
    public EmptyRequestBodyException() {
        super(ErrorCode.EMPTY_REQUEST_BODY, HttpStatus.BAD_REQUEST);
    }
}
