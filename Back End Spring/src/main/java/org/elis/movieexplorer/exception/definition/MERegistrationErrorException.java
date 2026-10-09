package org.elis.movieexplorer.exception.definition;

import java.io.Serial;

import org.springframework.http.HttpStatus;

public class MERegistrationErrorException extends MEBaseException {
	@Serial
    private static final long serialVersionUID = 7L;

	public MERegistrationErrorException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
