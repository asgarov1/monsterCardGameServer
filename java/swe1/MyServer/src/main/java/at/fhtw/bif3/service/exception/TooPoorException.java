package at.fhtw.bif3.service.exception;

public class TooPoorException extends RuntimeException {
    public TooPoorException(String message) {
        super(message);
    }
}
