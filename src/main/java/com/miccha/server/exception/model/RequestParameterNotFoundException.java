package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class RequestParameterNotFoundException extends MicchaException {
    public RequestParameterNotFoundException() {
        super(ErrorCode.SEARCH_QUERY_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }
}
