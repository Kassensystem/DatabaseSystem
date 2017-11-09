package dhbw.sa.databaseApplication.exceptions;

public class MySQLException extends Exception{

    public MySQLException() {
        super("MySQL-Server nicht erreichbar!");
    }

}
