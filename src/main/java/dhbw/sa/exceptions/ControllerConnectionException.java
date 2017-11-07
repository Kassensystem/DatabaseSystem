package dhbw.sa.exceptions;

public class ControllerConnectionException extends Exception {

    public ControllerConnectionException() {
        super("Rest-Api-Controller ist nicht erreichbar!");
    }
}
