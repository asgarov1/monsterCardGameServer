package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.controller.dto.TradingDTO;
import at.fhtw.bif3.domain.TradingDeal;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.domain.card.Card;
import at.fhtw.bif3.domain.card.DragonCard;
import at.fhtw.bif3.domain.card.GoblinCard;
import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.Response;
import at.fhtw.bif3.service.CardService;
import at.fhtw.bif3.service.TradingDealService;
import at.fhtw.bif3.service.UserService;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static at.fhtw.bif3.controller.TradingsController.TRADINGS_ENDPOINT;
import static at.fhtw.bif3.controller.util.ControllerTestUtil.*;
import static at.fhtw.bif3.domain.card.ElementType.FIRE;
import static at.fhtw.bif3.domain.card.ElementType.WATER;
import static at.fhtw.bif3.http.response.HttpStatus.NO_CONTENT;
import static at.fhtw.bif3.http.response.HttpStatus.OK;
import static at.fhtw.bif3.util.NumberUtil.randomInt;
import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TradingsControllerTest {

    private final TradingsController tradingsController = new TradingsController();
    private final TradingDealService tradingService = new TradingDealService();
    private final UserService userService = new UserService();
    private final CardService cardService = new CardService();

    private User user = new User("Tom", "pass");
    private Card card = new GoblinCard("tradingController_testId1", "test_name", randomInt(0, 100), WATER);
    private TradingDeal tradingDeal = new TradingDeal("tradingController_testId2", card, card.getType(), 50, user);

    @BeforeEach
    void setUp() {
        cardService.create(card);
        user.addCard(card);
        userService.create(user);
        SessionContext.loginUser(user.getUsername());
    }

    @AfterEach
    void clean() {
        SessionContext.logoutUser(user.getUsername());
        tradingService.delete(tradingDeal.getId());
        userService.delete(user);
    }

    @SneakyThrows
    @Test
    public void getShouldWork() {
        tradingService.create(tradingDeal);
        String getTradingDealsRequest = getRequest(TRADINGS_ENDPOINT, user.getUsername());
        Request httpRequest = HttpRequest.valueOf(new ByteArrayInputStream(getTradingDealsRequest.getBytes(StandardCharsets.UTF_8)));

        HttpResponse response = tradingsController.handleRequest(httpRequest);
        assertEquals(OK.getCode(), response.getStatusCode());

        var responseObject = stream(new Gson().fromJson(response.getContent(), TradingDTO[].class))
                .filter(dto -> dto.getId().equals(tradingDeal.getId()))
                .findFirst()
                .orElseThrow();

        assertEquals(tradingDeal.getCardToTrade().getId(), responseObject.getCardToTradeId());
        assertEquals(tradingDeal.getType().name(), responseObject.getType());
        assertEquals(tradingDeal.getId(), responseObject.getId());
        assertEquals(tradingDeal.getMinimumDamage(), responseObject.getMinimumDamage());
    }

    @SneakyThrows
    @Test
    public void createShouldWork() {
        int numberOfDealsBefore = tradingService.countEntities();

        String content = new Gson().toJson(new TradingDTO("tradingsControllerTest_tradeid", card.getId(), card.getType().name(), randomInt(0, 100)));
        String getTradingDealsRequest = postCreateRequestWithAuthorization(TRADINGS_ENDPOINT, user.getUsername(), content);
        Request httpRequest = HttpRequest.valueOf(new ByteArrayInputStream(getTradingDealsRequest.getBytes(StandardCharsets.UTF_8)));

        HttpResponse httpResponse = tradingsController.handleRequest(httpRequest);
        assertEquals(NO_CONTENT.getCode(), httpResponse.getStatusCode());

        assertEquals(numberOfDealsBefore + 1, tradingService.countEntities());
    }

    @Test
    public void deleteShouldWork() throws IOException {
        tradingService.create(tradingDeal);
        int numberOfDealsBefore = tradingService.countEntities();

        String endpoint = "/tradings/" + tradingDeal.getId();
        String getTradingDealsRequest = deleteRequest(endpoint, user.getUsername());
        Request httpRequest = HttpRequest.valueOf(new ByteArrayInputStream(getTradingDealsRequest.getBytes(StandardCharsets.UTF_8)));

        HttpResponse httpResponse = tradingsController.handleRequest(httpRequest);
        assertEquals(NO_CONTENT.getCode(), httpResponse.getStatusCode());

        assertEquals(numberOfDealsBefore - 1, tradingService.countEntities());
    }

    @Test
    public void tradeShouldWork() throws IOException {
        User dealCreator = user;
        user.lockCard(card);
        userService.update(user);
        tradingService.create(tradingDeal);

        User dealAccepter = new User("Adam", "pass");
        Card acceptersCard = new DragonCard("TradingControllerTest_offeredCardid123", "Drogo", tradingDeal.getMinimumDamage() + 1, FIRE);
        try {
            cardService.create(acceptersCard);
            dealAccepter.addCard(acceptersCard);
            userService.create(dealAccepter);
            SessionContext.loginUser(dealAccepter.getUsername());

            String tradeRequest = postCreateRequestWithAuthorization("/tradings/" + tradingDeal.getId(), dealAccepter.getUsername(), new Gson().toJson(acceptersCard.getId()));
            Request request = HttpRequest.valueOf(new ByteArrayInputStream(tradeRequest.getBytes(StandardCharsets.UTF_8)));

            Response response = tradingsController.handleRequest(request);
            assertEquals(NO_CONTENT.getCode(), response.getStatusCode());

            //updating entities
            dealCreator = userService.findById(dealCreator.getId());
            dealAccepter = userService.findById(dealAccepter.getId());

            assertEquals(tradingDeal.getCardToTrade(), dealAccepter.getCards().get(0));
            assertEquals(acceptersCard, dealCreator.getCards().get(0));
        } finally {
            cardService.delete(acceptersCard);
            userService.delete(dealAccepter);
        }
    }
}