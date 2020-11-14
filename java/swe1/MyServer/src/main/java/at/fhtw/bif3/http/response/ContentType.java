package at.fhtw.bif3.http.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContentType {
    APPLICATION_JSON ("application/json"),
    TEXT_HTML ("text/html");

    private final String name;
}
