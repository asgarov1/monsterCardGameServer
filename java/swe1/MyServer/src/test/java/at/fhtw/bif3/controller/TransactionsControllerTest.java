package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.domain.Bundle;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.domain.card.*;
import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.service.BundleService;
import at.fhtw.bif3.service.CardService;
import at.fhtw.bif3.service.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static at.fhtw.bif3.http.response.HttpStatus.*;
import static at.fhtw.bif3.service.UserService.CARD_PACKAGE_PRICE;
import static at.fhtw.bif3.util.PropertiesReader.getProperties;
import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionsControllerTest {

    private static final int PACKAGE_SIZE = parseInt(getProperties().getProperty("package.size"));
    private static final String BUNDLE_ID = "test_id";
    String acquirePackageRequest = "POST /transactions/packages HTTP/1.1\n" +
            "Authorization: Basic kienboec-mtcgToken\n" +
            "User-Agent: PostmanRuntime/7.26.8\n" +
            "Accept: */*\n" +
            "Postman-Token: 8cb5e3e8-d323-4377-9f96-6ce200807e2f\n" +
            "Host: localhost:10001\n" +
            "Accept-Encoding: gzip, deflate, br\n" +
            "Connection: keep-alive\n" +
            "Content-Length: 0\n";

    String badRequest = "POST /transactions/packages HTTP/1.1\n" +
            "Authorization: Basic baduser-mtcgToken\n" +
            "User-Agent: PostmanRuntime/7.26.8\n" +
            "Accept: */*\n" +
            "Postman-Token: 8cb5e3e8-d323-4377-9f96-6ce200807e2f\n" +
            "Host: localhost:10001\n" +
            "Accept-Encoding: gzip, deflate, br\n" +
            "Connection: keep-alive\n" +
            "Content-Length: 0\n";

    Request httpRequest;
    User user;
    List<Card> cards = new ArrayList<>();

    void setUpABundleForTest() {
        cards = List.of(
                new KnightCard("test_id1", "test_name1", new Random().nextDouble(), ElementType.FIRE),
                new SpellCard("test_id2", "test_name2", new Random().nextDouble(), ElementType.WATER),
                new OrkCard("test_id3", "test_name3", new Random().nextDouble(), ElementType.NORMAL),
                new KrakenCard("test_id4", "test_name4", new Random().nextDouble(), ElementType.NORMAL),
                new WizardCard("test_id5", "test_name5", new Random().nextDouble(), ElementType.NORMAL));
        var cardService = new CardService();
        cards.forEach(cardService::create);

        var bundleService = new BundleService();
        Bundle bundle = Bundle.builder().cards(cards).id(BUNDLE_ID).build();
        bundleService.create(bundle);
    }

    @AfterEach
    void cleanUp() {
        if(user != null){
            new UserService().delete(user.getId());
            user = null;
        }

        var cardService = new CardService();
        cards.forEach(cardService::delete);


        new BundleService().delete(BUNDLE_ID);
    }

    @SneakyThrows
    @Test
    public void handleTransactionsPackagesPostShouldFailForUnloggedUser() {
        setUpABundleForTest();

        httpRequest = HttpRequest.valueOf(new ByteArrayInputStream(badRequest.getBytes(StandardCharsets.UTF_8)));
        assertEquals(FORBIDDEN.getCode(), new TransactionController().handleRequest(httpRequest).getStatusCode());
    }

    @SneakyThrows
    @Test
    public void handleTransactionsPackagesPostShouldWorkOk() {
        setUpABundleForTest();

        UserService userService = new UserService();
        user = new User("kienboec", "daniel");
        userService.create(user);
        SessionContext.loginUser(user.getUsername());

        assertTrue(user.getCards().isEmpty());
        int numberOfCoinsBefore = user.getNumberOfCoins();

        httpRequest = HttpRequest.valueOf(new ByteArrayInputStream(acquirePackageRequest.getBytes(StandardCharsets.UTF_8)));
        assertEquals(NO_CONTENT.getCode(), new TransactionController().handleRequest(httpRequest).getStatusCode());

        user = userService.findByUsername(user.getUsername());
        assertEquals(PACKAGE_SIZE, user.getCards().size());
        assertEquals(numberOfCoinsBefore - CARD_PACKAGE_PRICE, user.getNumberOfCoins());
    }

    @SneakyThrows
    @DisplayName("Request to purchase packages should fail if not enough money")
    @Test
    public void handleTransactionsPackagesPostShouldFailIfNotEnoughMoney() {
        setUpABundleForTest();

        UserService userService = new UserService();
        user = new User("kienboec", "daniel");
        int numberOfCoinsBeforeTransaction = 2;
        user.setNumberOfCoins(numberOfCoinsBeforeTransaction);
        userService.create(user);
        SessionContext.loginUser(user.getUsername());

        assertTrue(user.getCards().isEmpty());

        httpRequest = HttpRequest.valueOf(new ByteArrayInputStream(acquirePackageRequest.getBytes(StandardCharsets.UTF_8)));
        assertEquals(INTERNAL_SERVER_ERROR.getCode(), new TransactionController().handleRequest(httpRequest).getStatusCode());

        user = userService.findByUsername(user.getUsername());
        assertTrue(user.getCards().isEmpty());
        assertEquals(numberOfCoinsBeforeTransaction, user.getNumberOfCoins());
    }

    @SneakyThrows
    @DisplayName("Request to purchase packages should fail if there is no package")
    @Test
    public void handleTransactionsPackagesShouldFailIfNoPackage() {
        UserService userService = new UserService();
        user = new User("kienboec", "daniel");

        userService.create(user);
        SessionContext.loginUser(user.getUsername());

        int numberOfCoinsBefore = user.getNumberOfCoins();
        assertTrue(user.getCards().isEmpty());

        httpRequest = HttpRequest.valueOf(new ByteArrayInputStream(acquirePackageRequest.getBytes(StandardCharsets.UTF_8)));
        assertEquals(INTERNAL_SERVER_ERROR.getCode(), new TransactionController().handleRequest(httpRequest).getStatusCode());

        user = userService.findByUsername(user.getUsername());
        assertTrue(user.getCards().isEmpty());
        assertEquals(numberOfCoinsBefore, user.getNumberOfCoins());
    }
}