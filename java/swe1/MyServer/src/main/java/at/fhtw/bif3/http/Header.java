package at.fhtw.bif3.http;

public enum Header {
    AUTHORIZATION("Authorization");

    private String name;

    Header(String name) {
        this.name = name;
    }
}
