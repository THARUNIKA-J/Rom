package com.cts.portal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Token not available")
public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException() {

    }
    public TokenInvalidException(String message) {
        super(message);
    }

}
