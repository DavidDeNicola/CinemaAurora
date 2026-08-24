package org.elis.movieexplorer.exception.definition;

import org.springframework.http.HttpStatus;

public class MENotAuthorizedException extends MEBaseException {
    public MENotAuthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
