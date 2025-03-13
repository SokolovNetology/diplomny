package logger;

public interface Logger {
    void log(LogStatus logStatus, String msg);

    static Logger getInstance() {
        return null;
    }
}
