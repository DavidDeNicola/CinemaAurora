package org.elis.movieexplorer.exception.definition;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class MENotFoundException extends MEBaseException {
	@Serial
    private static final long serialVersionUID = 5L;

	public MENotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
