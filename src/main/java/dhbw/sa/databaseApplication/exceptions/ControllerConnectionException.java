package dhbw.sa.databaseApplication.exceptions;

public class ControllerConnectionException extends Exception {

    public ControllerConnectionException() {
        super("Rest-Api-Controller ist nicht erreichbar!");
    }
}
