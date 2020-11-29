package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.response.Response;
import at.fhtw.bif3.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static at.fhtw.bif3.controller.SessionsController.SESSIONS_ENDPOINT;
import static at.fhtw.bif3.controller.util.ControllerTestUtil.postCreateRequest;
import static at.fhtw.bif3.http.request.HttpMethod.POST;
import static at.fhtw.bif3.http.response.HttpStatus.BAD_REQUEST;
import static at.fhtw.bif3.http.response.HttpStatus.NO_CONTENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SessionsControllerTest {

    private final User testUser = new User("jamesBond", "longLiveTheQueen");
    public final String CONTENT = "{\"Username\":\"" + testUser.getUsername() + "\", \"Password\":\"" + testUser.getPassword() + "\"}".replace('`', '"');
    private final String loginRequest = postCreateRequest(SESSIONS_ENDPOINT, CONTENT);
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