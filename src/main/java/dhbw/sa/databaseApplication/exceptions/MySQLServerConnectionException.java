package dhbw.sa.databaseApplication.exceptions;

        import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Verbindung zur Datenbank fehlgeschlagen!")
public class MySQLServerConnectionException extends RuntimeException{

    public MySQLServerConnectionException() {

    }
}
