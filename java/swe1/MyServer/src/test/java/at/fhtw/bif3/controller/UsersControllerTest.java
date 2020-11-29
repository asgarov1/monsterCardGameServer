package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.Response;
import at.fhtw.bif3.service.UserService;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static at.fhtw.bif3.controller.UsersController.USERS_ENDPOINT;
import static at.fhtw.bif3.controller.util.ControllerTestUtil.*;
import static at.fhtw.bif3.http.response.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UsersControllerTest {

    private final UserService userService = new UserService();
    private User user = new User("adam", "pass");

    @AfterEach
    void clean() {
        userService.delete(user.getUsername());
    }

    @SneakyThrows
    @Test
    public void postToUserShouldWork() {
        String CONTENT = "{\"Username\":\"" + user.getUsername() + "\", \"Password\":\"longLiveTheQueen\"}";
        String createUserRequest = postCreateRequest(USERS_ENDPOINT, CONTENT);
        Request httpRequest = HttpRequest.valueOf(new ByteArrayInputStream(createUserRequest.getBytes(StandardCharsets.UTF_8)));

        int usersBefore = userService.countEntities();

        Response httpResponse = new UsersController().handleRequest(httpRequest);
        assertEquals(CREATED.getCode(), httpResponse.getStatusCode());
        assertEquals(usersBefore + 1, userService.countEntities());

        usersBefore = userService.countEntities();
        httpResponse = new UsersController().handleRequest(httpRequest);
        assertEquals(BAD_REQUEST.getCode(), httpResponse.getStatusCode());
        assertEquals(usersBefore, userService.countEntities());
    }

    @SneakyThrows
    @Test
    public void getUserDataShouldWork() {
        userService.create(user);
        SessionContext.loginUser(user.getUsername());

        String getUserDataRequest = getRequest(USERS_ENDPOINT + "/" + user.getUsername(), user.getUsername());
        Request httpRequest = HttpRequest.valueOf(new ByteArrayInputStream(getUserDataRequest.getBytes(StandardCharsets.UTF_8)));

        HttpResponse httpResponse = new UsersController().handleRequest(httpRequest);
        assertEquals(OK.getCode(), httpResponse.getStatusCode());
        assertEquals(new Gson().fromJson(httpResponse.getContent(), User.class), user);
    }

    @SneakyThrows
    @Test
    public void editUserDataShouldWork() {
        userService.create(user);
        SessionContext.loginUser(user.getUsername());

        String newUsername = "newUsername";
        String content = "{\"Username\":\"" + newUsername  + "\"}";
        String getUserDataRequest = getPutRequest(USERS_ENDPOINT + "/" + user.getUsername(), user.getUsername(), content);
        Request httpRequest = HttpRequest.valueOf(new ByteArrayInputStream(getUserDataRequest.getBytes(StandardCharsets.UTF_8)));
        HttpResponse httpResponse = new UsersController().handleRequest(httpRequest);

        assertEquals(NO_CONTENT.getCode(), httpResponse.getStatusCode());
        assertEquals(userService.findById(user.getId()).getUsername(), newUsername);
    }
}
