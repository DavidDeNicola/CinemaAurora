package org.elis.movieexplorer.exception.definition;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class MEConflictException extends MEBaseException {
	@Serial
    private static final long serialVersionUID = 3L;

	public MEConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
