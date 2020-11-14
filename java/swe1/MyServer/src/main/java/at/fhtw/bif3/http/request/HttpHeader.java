package at.fhtw.bif3.http.request;

import lombok.Getter;

@Getter
public enum HttpHeader {
    CONTENT_TYPE ("Content-Type"),
    USER_AGENT ("User-Agent"),
    ACCEPT("Accept"),
    HOST("Host"),
    ACCEPT_ENCODING ("Accept-Encoding"),
    CONNECTION ("Connection"),
    CONTENT_LENGTH ("Content-Length");

    private final String name;

    HttpHeader(String headerName) {
        this.name = headerName;
    }

}
