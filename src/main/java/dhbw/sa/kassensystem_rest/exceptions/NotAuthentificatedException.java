package dhbw.sa.kassensystem_rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class NotAuthentificatedException extends RuntimeException
{
	public NotAuthentificatedException(String message)
	{
		super(message);
	}
}
