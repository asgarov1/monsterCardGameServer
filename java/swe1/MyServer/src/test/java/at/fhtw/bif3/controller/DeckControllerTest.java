package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static at.fhtw.bif3.controller.util.ControllerTestUtil.*;
import static at.fhtw.bif3.controller.DeckController.DECK_ENDPOINT;
import static at.fhtw.bif3.http.request.HttpMethod.GET;
import static at.fhtw.bif3.http.response.HttpStatus.OK;
import static org.junit.jupiter.api.Assertions.*;

class DeckControllerTest {

    User user = new User("kienboec", "password");
    String showDeckRequest = getRequest(GET.name(), DECK_ENDPOINT, user.getUsername());

    @SneakyThrows
    @Test
    void showDeckRequestShouldReturnDeck() {
        setUpUserWithCards(user);
        SessionContext.loginUser(user.getUsername());

        Request getDeckRequest = HttpRequest.valueOf(new ByteArrayInputStream(showDeckRequest.getBytes(StandardCharsets.UTF_8)));
        HttpResponse response = new DeckController().handleRequest(getDeckRequest);

        assertEquals(OK.getCode(), response.getStatusCode());
        deleteUserAndCards(user);
    }
}