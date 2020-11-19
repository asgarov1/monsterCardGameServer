package at.fhtw.bif3.http.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpStatus {
    OK                        (200, "OK"),
    CREATED                   (201, "Created"),
    ACCEPTED                  (202, "Accepted"),
    NO_CONTENT                (204, "No Content"),
    NOT_MODIFIED              (304, "Not Modified"),
    BAD_REQUEST               (400, "Bad Request"),
    UNAUTHORIZED           (401, "Unauthorized"),
    FORBIDDEN                 (403, "Forbidden"),
    NOT_FOUND                 (404, "Not Found"),
    INTERNAL_SERVER_ERROR     (500, "Internal Server Error"),
    NOT_IMPLEMENTED           (501, "Not Implemented");

    private final int code;
    private final String message;

}
