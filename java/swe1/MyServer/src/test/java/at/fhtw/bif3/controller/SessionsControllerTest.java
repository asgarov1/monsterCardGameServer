package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.Response;
import at.fhtw.bif3.service.UserService;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static at.fhtw.bif3.http.response.HttpStatus.BAD_REQUEST;
import static at.fhtw.bif3.http.response.HttpStatus.NO_CONTENT;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static java.lang.System.lineSeparator;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SessionsControllerTest {

    private final User testUser = new User("jamesBond", "longLiveTheQueen");
    public final String CONTENT = "{\"Username\":\"" + testUser.getUsername() + "\", \"Password\":\"" + testUser.getPassword() + "\"}".replace('`', '"');
    private final String loginRequest = "POST /sessions HTTP/1.1" + lineSeparator() +
            "Content-Type: application/json" + lineSeparator() +
            "User-Agent: PostmanRuntime/7.26.8" + lineSeparator() +
            "Accept: */*" + lineSeparator() +
            "Postman-Token: f292034f-2d7c-4f39-aff6-12d51a071002" + lineSeparator() +
            "Host: localhost:10001" + lineSeparator() +
            "Accept-Encoding: gzip, deflate, br" + lineSeparator() +
            "Connection: keep-alive" + lineSeparator() +
            "Content-Length: 55" + lineSeparator() + lineSeparator() +
            CONTENT + lineSeparator();

    HttpRequest httpRequest;

    @BeforeEach
    void init() {
        var userService = new UserService();
        userService.create(testUser);

        try {
            var inputStream = new ByteArrayInputStream(loginRequest.getBytes(StandardCharsets.UTF_8));
            httpRequest = HttpRequest.valueOf(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @AfterEach
    void cleanUp() {
        var userService = new UserService();
        userService.delete(testUser.getId());

        SessionContext.logoutUser(testUser.getUsername());
    }

    @Test
    @DisplayName("Login works as expected")
    public void loginWorks() {
        Response httpResponse = new SessionsController().handleRequest(httpRequest);
        assertEquals(httpResponse.getStatusCode(), NO_CONTENT.getCode());
        assertTrue(SessionContext.isUserLoggedIn(testUser.getUsername()));
    }

    @Test
    @DisplayName("After successful login secondary login attempts should return BAD_REQUEST")
    public void secondLoginDoesntWork() {
        Response httpResponse = new SessionsController().handleRequest(httpRequest);
        assertEquals(httpResponse.getStatusCode(), NO_CONTENT.getCode());
        assertTrue(SessionContext.isUserLoggedIn(testUser.getUsername()));

        httpResponse = new SessionsController().handleRequest(httpRequest);
        assertEquals(httpResponse.getStatusCode(), BAD_REQUEST.getCode());
        assertTrue(SessionContext.isUserLoggedIn(testUser.getUsername()));
    }

}