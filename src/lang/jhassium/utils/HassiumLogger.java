package lang.jhassium.utils;

import java.io.IOException;

/**
 * File : HassiumLogger.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 18:59
 */
public class HassiumLogger {
    public static void error(String message) {
        error(message, true, true, 1);
    }

    public static void error(String message, Boolean pause, Boolean exit, int exitCode) {
        writeMessage("ERROR", message, pause, exit, exitCode);
    }

    public static void info(String message) {
        info(message, false, false, 0);
    }

    public static void info(String message, Boolean pause, Boolean exit, int exitCode) {
        writeMessage("INFO", message, pause, exit, exitCode);
    }

    private static void writeMessage(String attribute, String message, Boolean pause, Boolean exit, int exitCode) {
        System.out.println(String.format("[%1s] %2s", attribute, message));
        if (pause) {
            System.out.println("Press a key to continue...");
            try {
                System.in.read();
            } catch (IOException e) {
            }
        }
        if (exit)
            System.exit(exitCode);
    }
}
