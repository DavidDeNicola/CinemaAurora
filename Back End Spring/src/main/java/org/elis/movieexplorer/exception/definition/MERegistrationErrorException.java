package org.elis.movieexplorer.exception.definition;

import java.io.Serial;

public class MERegistrationErrorException extends RuntimeException {
	@Serial
    private static final long serialVersionUID = 7L;

	public MERegistrationErrorException(String message) {
        super(message);
    }
}
