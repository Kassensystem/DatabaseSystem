package dhbw.sa.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class MySQLException extends Exception{

    public MySQLException() {
        super("MySQL-Server nicht erreichbar!");
    }

}
