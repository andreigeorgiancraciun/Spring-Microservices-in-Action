package com.optimagrowth.license.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchLicenceFoundException extends RuntimeException {

    public NoSuchLicenceFoundException(String message) {
        super(message);
    }
}
