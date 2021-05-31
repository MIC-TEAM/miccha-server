package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends MicchaException {
    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL, HttpStatus.CONFLICT);
    }
}
