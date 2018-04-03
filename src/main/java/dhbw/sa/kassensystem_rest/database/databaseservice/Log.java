package dhbw.sa.kassensystem_rest.database.databaseservice;

import org.joda.time.DateTime;

/**
 * Klasse mit Methoden für ein einheitliches Standard-Logging in die Konsole mit Zeitstempeln.
 *
 * @author Marvin Mai
 */
public class Log
{

	public static void logErr(String errorMessage)
	{
		log(errorMessage, "ERROR");
	}

	public static void logInf(String message)
	{
		log(message, "INFO");
	}

	public static void log(String message, String status)
	{
		String messageStatus = "";
		switch(status) {
			case "INFO": messageStatus = "MYSQL-Info";
				break;
			case "ERROR": messageStatus = "MYSQL-ERROR";
				break;
		}
		String logString = DateTime.now().toString("yyyy-MM-dd kk:mm:ss.SSS") + "  " + messageStatus + " " + message;
		System.out.println(logString);
	}

}
