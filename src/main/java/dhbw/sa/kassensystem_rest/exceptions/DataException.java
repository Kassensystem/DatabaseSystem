package dhbw.sa.kassensystem_rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Wird verwendet, wenn die an den DatabaseService gesendeten Daten nicht vollständig oder fehlerhaft sind.
 *
 * @author Marvin Mai
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class DataException extends RuntimeException{

    public DataException(String message) {
        super(message);
    }
}
