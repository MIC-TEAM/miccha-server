package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class EmailChangeTokenNotFoundException extends MicchaException {
    public EmailChangeTokenNotFoundException() {
        super(ErrorCode.EMAIL_CHANGE_TOKEN_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }
}
