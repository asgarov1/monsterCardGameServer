package at.fhtw.bif3.dao.exception;

public class DAOException extends RuntimeException {

    public DAOException() {
        super();
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(String message) {
        super(message);
    }
}
