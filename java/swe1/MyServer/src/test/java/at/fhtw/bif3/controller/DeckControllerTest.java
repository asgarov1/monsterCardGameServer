package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.domain.card.Card;
import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.service.UserService;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static at.fhtw.bif3.controller.DeckController.DECK_ENDPOINT;
import static at.fhtw.bif3.controller.util.ControllerTestUtil.*;
import static at.fhtw.bif3.http.request.HttpMethod.GET;
import static at.fhtw.bif3.http.request.HttpMethod.PUT;
import static at.fhtw.bif3.http.response.HttpStatus.NO_CONTENT;
import static at.fhtw.bif3.http.response.HttpStatus.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeckControllerTest {

    User user = new User("johnny", "password");

    @BeforeEach
    void init() {
        setUpUserWithCards(user, this.getClass().getName());
        SessionContext.loginUser(user.getUsername());
    }

    @AfterEach
    void cleanUp() {
        new UserService().delete(user);
        SessionContext.logoutUser(user.getUsername());
    }

    @SneakyThrows
    @Test
    void showDeckRequestShouldReturnDeck() {
        String showDeckRequest = getRequest(DECK_ENDPOINT, user.getUsername());
        Request getDeckRequest = HttpRequest.valueOf(new ByteArrayInputStream(showDeckRequest.getBytes(StandardCharsets.UTF_8)));
        HttpResponse response = new DeckController().handleRequest(getDeckRequest);

        assertEquals(OK.getCode(), response.getStatusCode());
    }

    @SneakyThrows
    @Test
    void showDeckRequestPLainShouldWork() {
        String showDeckRequest = getRequest(DECK_ENDPOINT + "?format=plain", user.getUsername());
        Request getDeckRequest = HttpRequest.valueOf(new ByteArrayInputStream(showDeckRequest.getBytes(StandardCharsets.UTF_8)));
        HttpResponse response = new DeckController().handleRequest(getDeckRequest);

        assertEquals(OK.getCode(), response.getStatusCode());
        assertEquals(user.getDeck().toString(), response.getContent());
    }

    @SneakyThrows
    @Test
    void configureDeckRequestShouldWorkOk() {
        List<String> cardIds = getFourRandomCardIds(user);

        String configureDeckRequest = getPutRequest(DECK_ENDPOINT, user.getUsername(), new Gson().toJson(cardIds));
        Request request = HttpRequest.valueOf(new ByteArrayInputStream(configureDeckRequest.getBytes(StandardCharsets.UTF_8)));
        HttpResponse response = new DeckController().handleRequest(request);

        assertEquals(NO_CONTENT.getCode(), response.getStatusCode());

        user = new UserService().findById(user.getId());
        user.getDeck().forEach(card -> assertTrue(cardIds.contains(card.getId())));
        assertEquals(cardIds.size(), user.getDeck().size());
    }

    private List<String> getFourRandomCardIds(User user) {
        return user.getCards()
                .stream()
                .map(Card::getId)
                .limit(4)
                .collect(Collectors.toList());
    }
}