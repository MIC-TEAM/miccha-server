package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class MicchaException extends RuntimeException {
    private ErrorCode errorCode;
    private HttpStatus httpResponseStatus;
}
