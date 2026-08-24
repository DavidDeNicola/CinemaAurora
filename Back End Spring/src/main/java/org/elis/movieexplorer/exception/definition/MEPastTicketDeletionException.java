package org.elis.movieexplorer.exception.definition;

import java.io.Serial;

import org.springframework.http.HttpStatus;

public class MEPastTicketDeletionException extends MEBaseException {
	@Serial
    private static final long serialVersionUID = 2L;

    public MEPastTicketDeletionException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
