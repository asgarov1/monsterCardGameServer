package at.fhtw.bif3.service.exception;

public class TooPoorException extends RuntimeException {

    public TooPoorException() {
        super();
    }

    public TooPoorException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooPoorException(String message) {
        super(message);
    }
}
