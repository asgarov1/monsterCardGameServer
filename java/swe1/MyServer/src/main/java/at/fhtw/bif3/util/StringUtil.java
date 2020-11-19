package at.fhtw.bif3.util;

public class StringUtil {
    public static String extractUsername(String contentString) {
        String withoutBrackets = contentString.replace("{", "").replace("}", "");
        String usernameLine = withoutBrackets.split(",")[0];
        return usernameLine.split(":")[1].replace('"', ' ').trim();
    }

    public static String extractPassword(String contentString) {
        String withoutBrackets = contentString.replace("{", "").replace("}", "");
        String passwordLine = withoutBrackets.split(",")[1];
        return passwordLine.split(":")[1].replace('"', ' ').trim();
    }

    public static String extractToken(String authorizationHeader) {
        //Basic kienboec-mtcgToken
        return authorizationHeader.replace("Basic ", "");
    }
}
