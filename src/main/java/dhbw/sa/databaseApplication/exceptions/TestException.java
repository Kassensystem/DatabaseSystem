package dhbw.sa.databaseApplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Das ist die Message einer Testexception!")
public class TestException extends RuntimeException {

    public TestException() {
        super();
    }
}