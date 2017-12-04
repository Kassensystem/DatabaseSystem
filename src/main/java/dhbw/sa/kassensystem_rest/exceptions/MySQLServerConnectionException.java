package dhbw.sa.kassensystem_rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Wird verwendet, wenn die Verbindung des DatabaseServices zu MySQL-Datenbank fehlgeschlagen ist.
 *
 * @author Marvin Mai
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class MySQLServerConnectionException extends RuntimeException{

    public MySQLServerConnectionException() {
        super("Verbindung zur Datenbank fehlgeschlagen!");
    }
}
