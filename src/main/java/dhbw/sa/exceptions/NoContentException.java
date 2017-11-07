package dhbw.sa.exceptions;

public class NoContentException extends Exception{

    public NoContentException() {
        super("Tabelle hat keinen Inhalt!");
    }
}
