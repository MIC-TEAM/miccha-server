package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class NameUpdateFailedException extends MicchaException {
    public NameUpdateFailedException() {
        super(ErrorCode.NAME_UPDATE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
