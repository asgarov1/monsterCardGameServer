package at.fhtw.bif3.service.exception;

public class TransactionException extends RuntimeException {
    public TransactionException() {
        super();
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(String message) {
        super(message);
    }
}
