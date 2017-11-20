package dhbw.sa.databaseApplication.exceptions;

import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Wird verwendet, wenn eine bestehende Bestellung, die geupdatet werden soll,
 * vom DatabaseService in der MySQL-Datenbank nicht gefunden werden kann.
 *
 * @author Marvin Mai
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Bestellung nicht gefunden!")
public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException() {

    }
}
