package com;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;
import java.util.logging.Formatter;

/**
 * Primary Driver class for CPSC599.44-MachineLearning project. Initializes logging structures for
 * overall application in this static class. This class has the following attributes:
 * <ul>
 *     <li>{@link #LOGGER}</li>
 * </ul>
 *
 * TODO declare configurations file: config.txt and specify format
 * TODO make logger messages receivable from alternative executing classes
 *
 * @author Andrew Burton
 * @version %I%, %G%
 * @since 1.0
 */
public class Main {

    /** The {@link Logger} identifier used to setup logger streams for recording data messages.*/
    private static Logger LOGGER = Logger.getLogger("MainLogger");

    /**
     * The main function is the default runner for this application. This function will be responsible for
     * receiving error codes and logging to the appropriate location, as well as controlling program flow.
     *
     * @param args commandline arguments provided at runtime
     */
    public static void main(String[] args) {
	    // Validate appropriate arguments have been supplied
        if (args.length == 0) {
            System.out.println("Usage: java Main --args [app-args]");
            System.exit(-1);
        } else {
            Map<String, String> argParse = parseArgs(args);
            LOGGER.info(argParse.toString());
            // Once arguments have been validated and processed, begin main program execution flow
            try {
                setupLogger();
                LOGGER.setLevel(Level.ALL);
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
                System.exit(-1);
            }
        }
        System.out.println("Hello, World!");
    }

    /**
     * The parseArgs function is provided a list of arguments provided to the application, and transforms it
     * into a map of flags and values.
     *
     * @param args commandline arguments passed from main
     * @return the list of arguments parsed by their assigned flags
     */
    private static Map<String,String> parseArgs(String[] args) {
        LOGGER.info(Arrays.toString(args));
        return new HashMap<>();
    }

    /**
     * Method to create handler objects for the log controller, in order to specify where to write log
     * messages.
     *
     * @throws IOException pass error upwards from contained methods
     */
    private static void setupLogger() throws IOException {
        LOGGER.setUseParentHandlers(false);
        Formatter formatter = setupFormatLog();
        setupConsoleLog(formatter);
        setupFileLog(formatter);
        LOGGER.info("Logger Initialized");
    }

    /**
     * Setup the logger handler for writing log messages to the console.
     *
     * @param formatter sets structure for log messages
     */
    private static void setupConsoleLog(Formatter formatter) {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(formatter);
        LOGGER.addHandler(consoleHandler);
    }

    /**
     * Setup the logger handler for writing log messages to a given file for the associated class
     *
     * @param formatter sets structure for log messages
     * @throws IOException caused by an invalid access to the logs/main.log log file
     */
    private static void setupFileLog(Formatter formatter) throws IOException {
        FileHandler fileHandler = new FileHandler("logs/main.log", true);
        fileHandler.setFormatter(formatter);
        LOGGER.addHandler(fileHandler);
    }

    /**
     * A simple method designed to create a basic structure for the log messages containing date, time, calling class,
     * calling method, the message level, as well as the provided log message.  These details can be used to help pinpoint
     * the source of a given problem.
     *
     * @return formatter object to set the default shape of each logged message
     */
    private static Formatter setupFormatLog() {
        return new Formatter() {
            @Override
            public String format(LogRecord record) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                Calendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(record.getMillis());
                return record.getLevel()
                        + " - ["
                        + simpleDateFormat.format(calendar.getTime())
                        + "] || "
                        + record.getSourceClassName().substring(record.getSourceClassName().indexOf(".") + 1)
                        + "."
                        + record.getSourceMethodName()
                        + "() : "
                        + record.getMessage()
                        + "\n";
            }
        };
    }
}
