package at.fhtw.bif3.controller;

import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.Response;
import at.fhtw.bif3.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static at.fhtw.bif3.controller.UsersController.USERS_ENDPOINT;
import static at.fhtw.bif3.controller.util.ControllerTestUtil.postCreateRequest;
import static at.fhtw.bif3.http.request.HttpMethod.POST;
import static at.fhtw.bif3.http.response.HttpStatus.BAD_REQUEST;
import static at.fhtw.bif3.http.response.HttpStatus.CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UsersControllerTest {

    private final UserService userService = new UserService();

    private final String userName = "jamesBond";
    public final String CONTENT = "{\"Username\":\"" + userName + "\", \"Password\":\"longLiveTheQueen\"}".replace('`', '"');
    private final String createUserRequest = postCreateRequest(POST.name(), USERS_ENDPOINT, CONTENT);

    Request httpRequest;

    @BeforeEach
    public void init() {
        try {
            var inputStream = new ByteArrayInputStream(createUserRequest.getBytes(StandardCharsets.UTF_8));
            httpRequest = HttpRequest.valueOf(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void cleanUp() {
        userService.delete(userName);
    }

    @Test
    public void postToUserShouldWork() {
        int usersBefore = userService.countEntities();

        Response httpResponse = new UsersController().handleRequest(httpRequest);
        assertEquals(CREATED.getCode(), httpResponse.getStatusCode());
        assertEquals(usersBefore + 1, userService.countEntities());

        usersBefore = userService.countEntities();
        httpResponse = new UsersController().handleRequest(httpRequest);
        assertEquals(BAD_REQUEST.getCode(), httpResponse.getStatusCode());
        assertEquals(usersBefore, userService.countEntities());
    }

}