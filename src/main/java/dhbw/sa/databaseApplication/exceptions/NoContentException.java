package dhbw.sa.databaseApplication.exceptions;

public class NoContentException extends Exception{

    public NoContentException() {
        super("Tabelle hat keinen Inhalt!");
    }
}
