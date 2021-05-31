package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class RequestMissingEmailException extends MicchaException {
    public RequestMissingEmailException() {
        super(ErrorCode.REQUEST_MISSING_EMAIL, HttpStatus.BAD_REQUEST);
    }
}
