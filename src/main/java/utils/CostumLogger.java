package utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CostumLogger {
    private final Logger logger = Logger.getLogger(CostumLogger.class.getName());
    private FileHandler fileHandler = null;
    public static final String LOGGINGFILE = "logFile.log";
    private static CostumLogger me;

    protected CostumLogger() {
        try {
            fileHandler = new FileHandler(LOGGINGFILE, true);
        } catch (SecurityException | IOException e) {
            CostumLogger.getInstance().logError(e);
            return;
        }
        Logger l = Logger.getLogger("");
        fileHandler.setFormatter(new SimpleFormatter());
        l.addHandler(fileHandler);

    }

    public static CostumLogger getInstance() {
        if (me != null && me.fileHandler != null) {
            return me;
        } else if (me != null) reset();
        me = new CostumLogger();
        return me;
    }

    public static void reset() {
        if (me != null && me.fileHandler != null) {
            me.logger.removeHandler(me.fileHandler);
            me = null;
        }
    }

    public void logError(Exception error) {
        if (fileHandler != null) logger.log(Level.WARNING,
                //() -> Arrays.toString(error.getStackTrace())
                error.getMessage()
        );
    }

    public void logString(String toLog) {
        if (fileHandler != null) logger.log(Level.WARNING, toLog);
    }
}
