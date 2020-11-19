package at.fhtw.bif3.controller.context;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SessionContext {
    public static final String TOKEN_ENDING = "-mtcgToken";
    private static volatile ConcurrentMap<String, String> loggedUsers = new ConcurrentHashMap<>();

    public static boolean isUserLoggedIn(String username) {
        return loggedUsers.keySet().stream().anyMatch(username::equals);
    }

    public static boolean isTokenPresent(String token) {
        return loggedUsers.values().stream().anyMatch(token::equals);
    }

    public static void loginUser(String username) {
        loggedUsers.putIfAbsent(username, getTokenForUsername(username));
    }

    private static String getTokenForUsername(String username) {
        return username + TOKEN_ENDING;
    }
}
