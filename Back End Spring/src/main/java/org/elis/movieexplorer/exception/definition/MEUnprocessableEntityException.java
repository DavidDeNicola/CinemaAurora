package org.elis.movieexplorer.exception.definition;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class MEUnprocessableEntityException extends MEBaseException {
	@Serial
    private static final long serialVersionUID = 7L;

    public MEUnprocessableEntityException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
