package com.miccha.server.exception.model;

import com.miccha.server.ErrorCode;
import org.springframework.http.HttpStatus;

public class RequestInvalidMovieIdException extends MicchaException {
    public RequestInvalidMovieIdException() {
        super(ErrorCode.INVALID_MOVIE_ID, HttpStatus.BAD_REQUEST);
    }
}
