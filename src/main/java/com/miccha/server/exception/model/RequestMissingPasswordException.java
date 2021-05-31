package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class RequestMissingPasswordException extends MicchaException {
    public RequestMissingPasswordException() {
        super(ErrorCode.REQUEST_MISSING_PASSWORD, HttpStatus.BAD_REQUEST);
    }
}
