package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class EmailNotFoundException extends MicchaException {
    public EmailNotFoundException() {
        super(ErrorCode.EMAIL_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
