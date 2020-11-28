package at.fhtw.bif3.controller;

import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.Response;
import at.fhtw.bif3.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static at.fhtw.bif3.http.response.HttpStatus.BAD_REQUEST;
import static at.fhtw.bif3.http.response.HttpStatus.CREATED;
import static java.lang.System.lineSeparator;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UsersControllerTest {

    private final UserService userService = new UserService();

    private final String userName = "jamesBond";
    public final String CONTENT = "{`Username`:`" + userName + "`, `Password`:`longLiveTheQueen`}".replace('`', '"');
    private final String createUserRequest = "POST /users HTTP/1.1" + lineSeparator() +
            "Content-Type: application/json" + lineSeparator() +
            "User-Agent: PostmanRuntime/7.26.8" + lineSeparator() +
            "Accept: */*" + lineSeparator() +
            "Postman-Token: f292034f-2d7c-4f39-aff6-12d51a071002" + lineSeparator() +
            "Host: localhost:10001" + lineSeparator() +
            "Accept-Encoding: gzip, deflate, br" + lineSeparator() +
            "Connection: keep-alive" + lineSeparator() +
            "Content-Length: 55" + lineSeparator() + lineSeparator() +
            CONTENT + lineSeparator();


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
        assertEquals(httpResponse.getStatusCode(), CREATED.getCode());
        assertEquals(usersBefore + 1, userService.countEntities());

        usersBefore = userService.countEntities();
        httpResponse = new UsersController().handleRequest(httpRequest);
        assertEquals(httpResponse.getStatusCode(), BAD_REQUEST.getCode());
        assertEquals(usersBefore, userService.countEntities());
    }

}