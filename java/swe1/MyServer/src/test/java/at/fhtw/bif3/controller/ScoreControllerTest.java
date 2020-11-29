package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.controller.dto.ScoreDTO;
import at.fhtw.bif3.controller.dto.StatsDTO;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.service.UserService;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static at.fhtw.bif3.controller.ScoreController.SCORE_ENDPOINT;
import static at.fhtw.bif3.controller.StatsController.STATS_ENDPOINT;
import static at.fhtw.bif3.controller.util.ControllerTestUtil.getRequest;
import static at.fhtw.bif3.http.response.HttpStatus.OK;
import static org.junit.jupiter.api.Assertions.*;

class ScoreControllerTest {

    private final UserService userService = new UserService();
    private final User user = new User("adam", "pass");

    @AfterEach
    void clean() {
        userService.delete(user.getUsername());
    }

    @Test
    void getScoreboardShouldWork() throws IOException {
        userService.create(user);
        SessionContext.loginUser(this.user.getUsername());

        String getScoreRequest = getRequest(SCORE_ENDPOINT, this.user.getUsername());
        Request httpRequest = HttpRequest.valueOf(new ByteArrayInputStream(getScoreRequest.getBytes(StandardCharsets.UTF_8)));

        HttpResponse httpResponse = new ScoreController().handleRequest(httpRequest);
        assertEquals(OK.getCode(), httpResponse.getStatusCode());
        assertEquals(new Gson().fromJson(httpResponse.getContent(), ScoreDTO[].class).length, userService.countEntities());
    }
}