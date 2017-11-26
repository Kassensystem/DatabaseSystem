package dhbw.sa.kassensystem_rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Wird verwendet, wenn eine Tabelle vom DatabaseService angefordert wird aber keinen Inhalt hat.
 *
 * @author Marvin Mai
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Tabelle hat keinen Inhalt!")
public class NoContentException extends RuntimeException{

    public NoContentException() {

    }
}
