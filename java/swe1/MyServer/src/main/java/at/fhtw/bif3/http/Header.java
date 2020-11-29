package at.fhtw.bif3.http;

import lombok.Getter;

public enum Header {
    AUTHORIZATION("Authorization");

    @Getter
    private final String name;

    Header(String name) {
        this.name = name;
    }
}
