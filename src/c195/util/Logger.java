package c195.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

/**
 * @author Jonathan Dowdell
 */
public class Logger {
    private static final String FILENAME = "login_activity.text";

    public Logger() {}

    public static void log(String username, Boolean success, String message) {
        try (FileWriter fileWriter = new FileWriter(FILENAME, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println(username + (success ? " Successful" : " Failed") + " " + message + " " + Instant.now().toString()) ;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
