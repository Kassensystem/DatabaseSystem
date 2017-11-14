package dhbw.sa.databaseApplication.exceptions;

        import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Bestellung nicht gefunden!")
public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException() {

    }
}
