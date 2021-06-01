package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class RequestMissingTokenException extends MicchaException {
    public RequestMissingTokenException() {
        super(ErrorCode.REQUEST_MISSING_TOKEN, HttpStatus.BAD_REQUEST);
    }
}
