package com.cts.componentprocessing.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Invalid token..")
public class TokenInvalidException extends RuntimeException{
    public TokenInvalidException(String msg) {
        super(msg);
    }
}
