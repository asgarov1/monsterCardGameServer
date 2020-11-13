package at.fhtw.bif3.http.response;

public enum HttpStatus {
    OK                        (200),
    NO_CONTENT                (204),
    NOT_MODIFIED              (304),
    BAD_REQUEST               (340),
    FORBIDDEN                 (403),
    NOT_FOUND                 (404),
    INTERNAL_SERVER_ERROR     (500),
    NOT_IMPLEMENTED           (501);

    private final int statusCode;

    HttpStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
