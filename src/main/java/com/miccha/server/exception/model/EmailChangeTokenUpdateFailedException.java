package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class EmailChangeTokenUpdateFailedException extends MicchaException {
    public EmailChangeTokenUpdateFailedException() {
        super(ErrorCode.EMAIL_CHANGE_TOKEN_UPDATE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
