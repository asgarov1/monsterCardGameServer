package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.service.UserService;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static at.fhtw.bif3.controller.CardController.CARDS_ENDPOINT;
import static at.fhtw.bif3.controller.util.ControllerTestUtil.*;
import static at.fhtw.bif3.http.request.HttpMethod.GET;
import static at.fhtw.bif3.http.response.HttpStatus.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CardControllerTest {

    User user = new User("kienboec", "password");
    String showCardsRequest = getRequest(CARDS_ENDPOINT, user.getUsername());

    @SneakyThrows
    @Test
    void showCardsRequestShouldReturnUsersCards() {
        setUpUserWithCards(user, this.getClass().getName());
        SessionContext.loginUser(user.getUsername());

        Request getCardsRequest = HttpRequest.valueOf(new ByteArrayInputStream(showCardsRequest.getBytes(StandardCharsets.UTF_8)));
        HttpResponse response = new CardController().handleRequest(getCardsRequest);

        assertEquals(OK.getCode(), response.getStatusCode());
        assertEquals(new Gson().toJson(user.getCards()).length(), response.getContentLength());

        new UserService().delete(user);
    }
}