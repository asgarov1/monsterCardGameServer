package at.fhtw.bif3.util;

import at.fhtw.bif3.domain.User;
import com.google.gson.GsonBuilder;

public class StringUtil {
    public static String extractUsername(String contentString) {
        return new GsonBuilder().create().fromJson(contentString, User.class).getUsername();
    }

    public static String extractPassword(String contentString) {
        return new GsonBuilder().create().fromJson(contentString, User.class).getPassword();
    }

    public static String extractToken(String authorizationHeader) {
        //Basic kienboec-mtcgToken
        return authorizationHeader.replace("Basic ", "");
    }

    public static String extractUsernameFromToken(String token) {
        return token.substring(0, token.indexOf("-"));
    }
}
