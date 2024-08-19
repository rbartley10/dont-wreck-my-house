package learn.myhouse.data;

public class DataException extends Exception{
    public DataException(String message) {
        super(message);
    } //accepts message

    public DataException(Throwable cause) {
        super(cause);
    } //accepts cause

    public DataException(String message, Throwable cause) {
        super(message, cause);
    } //accepts message and cause
}
