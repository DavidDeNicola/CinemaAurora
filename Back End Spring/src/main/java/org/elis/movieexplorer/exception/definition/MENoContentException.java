package org.elis.movieexplorer.exception.definition;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class MENoContentException extends MEBaseException {
	@Serial
    private static final long serialVersionUID = 4L;

	public MENoContentException(String message) {
        super(message, HttpStatus.NO_CONTENT);
    }
}
