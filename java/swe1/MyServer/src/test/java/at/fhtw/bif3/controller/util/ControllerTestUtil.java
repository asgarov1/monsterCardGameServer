package at.fhtw.bif3.controller.util;

import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.domain.card.*;
import at.fhtw.bif3.service.CardService;
import at.fhtw.bif3.service.UserService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static at.fhtw.bif3.util.NumberUtil.randomInt;
import static java.lang.System.lineSeparator;

public class ControllerTestUtil {
    public static String getRequest(String path, String username) {
        return "GET " + path + " HTTP/1.1\n" +
                "Authorization: Basic " + username + "-mtcgToken\n" +
                "User-Agent: PostmanRuntime/7.26.8\n" +
                "Accept: */*\n" +
                "Postman-Token: 8cb5e3e8-d323-4377-9f96-6ce200807e2f\n" +
                "Host: localhost:10001\n" +
                "Accept-Encoding: gzip, deflate, br\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 0\n";
    }

    public static String deleteRequest(String path, String username) {
        return "DELETE " + path + " HTTP/1.1\n" +
                "Authorization: Basic " + username + "-mtcgToken\n" +
                "User-Agent: PostmanRuntime/7.26.8\n" +
                "Accept: */*\n" +
                "Postman-Token: 8cb5e3e8-d323-4377-9f96-6ce200807e2f\n" +
                "Host: localhost:10001\n" +
                "Accept-Encoding: gzip, deflate, br\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 0\n";
    }

    public static void setUpUserWithCards(User user, String className) {
        List<Card> cards = Stream.of(
                new KnightCard(className + "test_id1", "test_name1", randomInt(0, 100), ElementType.FIRE),
                new SpellCard(className + "test_id2", "test_name2", randomInt(0, 100), ElementType.WATER),
                new OrkCard(className + "test_id3", "test_name3", randomInt(0, 100), ElementType.NORMAL),
                new WizardCard(className + "test_id4", "test_name4", randomInt(0, 100), ElementType.FIRE),
                new SpellCard(className + "test_id5", "test_name5", randomInt(0, 100), ElementType.NORMAL)).collect(Collectors.toList());
        CardService cardService = new CardService();
        cards.forEach(cardService::create);

        user.setCards(cards);
        UserService userService = new UserService();
        userService.create(user);
    }

    public static String postCreateRequest(String endpoint, String content) {
        return "POST " + endpoint + " HTTP/1.1" + lineSeparator() +
                "Content-Type: application/json" + lineSeparator() +
                "User-Agent: PostmanRuntime/7.26.8" + lineSeparator() +
                "Accept: */*" + lineSeparator() +
                "Postman-Token: f292034f-2d7c-4f39-aff6-12d51a071002" + lineSeparator() +
                "Host: localhost:10001" + lineSeparator() +
                "Accept-Encoding: gzip, deflate, br" + lineSeparator() +
                "Connection: keep-alive" + lineSeparator() +
                "Content-Length: " + content.length() + lineSeparator() + lineSeparator() +
                content + lineSeparator();
    }

    public static String postCreateRequestWithAuthorization(String endpoint, String username, String content) {
        return "POST " + endpoint + " HTTP/1.1" + lineSeparator() +
                "Authorization: Basic " + username + "-mtcgToken\n" +
                "Content-Type: application/json" + lineSeparator() +
                "User-Agent: PostmanRuntime/7.26.8" + lineSeparator() +
                "Accept: */*" + lineSeparator() +
                "Postman-Token: f292034f-2d7c-4f39-aff6-12d51a071002" + lineSeparator() +
                "Host: localhost:10001" + lineSeparator() +
                "Accept-Encoding: gzip, deflate, br" + lineSeparator() +
                "Connection: keep-alive" + lineSeparator() +
                "Content-Length: " + content.length() + lineSeparator() + lineSeparator() +
                content + lineSeparator();
    }

    public static String getPutRequest(String endpoint, String username, String content) {
        return "PUT " + endpoint + " HTTP/1.1\n" +
                "Authorization: Basic " + username + "-mtcgToken\n" +
                "Content-Type: application/json\n" +
                "User-Agent: PostmanRuntime/7.26.8\n" +
                "Accept: */*\n" +
                "Postman-Token: f292034f-2d7c-4f39-aff6-12d51a071002\n" +
                "Host: localhost:10001\n" +
                "Accept-Encoding: gzip, deflate, br\n" +
                "Connection: keep-alive\n" +
                "Content-Length: " + content.length() + "\n" +
                content + "\n";
    }

    public static String postNoContentRequest(String endpoint, String username) {
        return "POST " + endpoint + " HTTP/1.1\n" +
                "Authorization: Basic " + username + "-mtcgToken\n" +
                "User-Agent: PostmanRuntime/7.26.8\n" +
                "Accept: */*\n" +
                "Postman-Token: 8cb5e3e8-d323-4377-9f96-6ce200807e2f\n" +
                "Host: localhost:10001\n" +
                "Accept-Encoding: gzip, deflate, br\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 0\n";
    }
}
