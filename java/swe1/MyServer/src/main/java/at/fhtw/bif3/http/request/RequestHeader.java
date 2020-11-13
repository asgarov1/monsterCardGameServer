package at.fhtw.bif3.http.request;

import lombok.Getter;

@Getter
public enum RequestHeader {
    USER_AGENT ("User-Agent"),
    CONTENT_LENGTH ("Content-Length"),
    CONTENT_TYPE ("Content-Type");

    private final String name;

    RequestHeader(String headerName) {
        this.name = headerName;
    }
}
