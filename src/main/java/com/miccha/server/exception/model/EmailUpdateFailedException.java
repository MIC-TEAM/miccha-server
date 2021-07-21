package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class EmailUpdateFailedException extends MicchaException {
    public EmailUpdateFailedException() {
        super(ErrorCode.EMAIL_UPDATE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
