package dhbw.sa.databaseApplication.exceptions;

import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Wird verwendet, wenn die Verbindung des DatabaseServices zu MySQL-Datenbank fehlgeschlagen ist.
 *
 * @author Marvin Mai
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Verbindung zur Datenbank fehlgeschlagen!")
public class MySQLServerConnectionException extends RuntimeException{

    public MySQLServerConnectionException() {

    }
}
