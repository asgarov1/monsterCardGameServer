package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.controller.dto.StatsDTO;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.service.UserService;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static at.fhtw.bif3.controller.StatsController.STATS_ENDPOINT;
import static at.fhtw.bif3.controller.util.ControllerTestUtil.getRequest;
import static at.fhtw.bif3.http.response.HttpStatus.OK;
import static org.junit.jupiter.api.Assertions.*;

class StatsControllerTest {

    private final UserService userService = new UserService();
    private final User user = new User("adam", "pass");

    @AfterEach
    void clean() {
        userService.delete(user.getUsername());
    }

    @SneakyThrows
    @Test
    void getStatsShouldWork(){
        userService.create(user);
        SessionContext.loginUser(user.getUsername());

        String getStatsRequest = getRequest(STATS_ENDPOINT, user.getUsername());
        Request httpRequest = HttpRequest.valueOf(new ByteArrayInputStream(getStatsRequest.getBytes(StandardCharsets.UTF_8)));

        HttpResponse httpResponse = new StatsController().handleRequest(httpRequest);
        assertEquals(OK.getCode(), httpResponse.getStatusCode());
        assertEquals(new Gson().fromJson(httpResponse.getContent(), StatsDTO.class), new StatsDTO(user.getGamesPlayed(), user.getElo()));
    }
}
