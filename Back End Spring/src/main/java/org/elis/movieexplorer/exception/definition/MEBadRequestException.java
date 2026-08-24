package org.elis.movieexplorer.exception.definition;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class MEBadRequestException extends MEBaseException {
	@Serial
    private static final long serialVersionUID = 2L;

    public MEBadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
