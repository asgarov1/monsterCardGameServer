package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.domain.card.*;
import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.Response;
import at.fhtw.bif3.service.BattleManager;
import at.fhtw.bif3.service.CardService;
import at.fhtw.bif3.service.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static at.fhtw.bif3.controller.BattlesController.BATTLES_ENDPOINT;
import static at.fhtw.bif3.controller.util.ControllerTestUtil.getRequest;
import static at.fhtw.bif3.controller.util.ControllerTestUtil.postNoContentRequest;
import static at.fhtw.bif3.http.response.HttpStatus.NO_CONTENT;
import static at.fhtw.bif3.http.response.HttpStatus.OK;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BattlesControllerTest {

    ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(2);

    BattlesController battlesController = new BattlesController();
    List<Card> cardsA = Stream.of(
            new ElfCard("test_id1", "Firemonster", new Random().nextInt(100), ElementType.FIRE),
            new SpellCard("test_id2", "Water Spell", new Random().nextInt(100), ElementType.WATER),
            new DragonCard("test_id3", "Earth Elementel", new Random().nextInt(100), ElementType.NORMAL),
            new WizardCard("test_id4", "Werewolf", new Random().nextInt(100), ElementType.NORMAL)).collect(toList());

    List<Card> cardsB = Stream.of(
            new DragonCard("test_id5", "Fire Dragon", new Random().nextInt(100), ElementType.FIRE),
            new SpellCard("test_id6", "Ice Spell", new Random().nextInt(100), ElementType.WATER),
            new GoblinCard("test_id7", "Tigerman", new Random().nextInt(100), ElementType.NORMAL),
            new OrkCard("test_id8", "Cursed Tree", new Random().nextInt(100), ElementType.NORMAL)).collect(toList());

    User userA = new User("userA", "pass");
    User userB = new User("userB", "pass");

    UserService userService = new UserService();

    @BeforeEach
    void setUp(){
        CardService cardService = new CardService();
        cardsA.forEach(cardService::create);
        cardsB.forEach(cardService::create);

        userA.setCards(cardsA);
        userB.setCards(cardsB);

        userA.generateBattleDeck();
        userB.generateBattleDeck();

        userService.create(userA);
        userService.create(userB);

        SessionContext.loginUser(userA.getUsername());
        SessionContext.loginUser(userB.getUsername());
    }

    @AfterEach
    void clean(){
        SessionContext.logoutUser(userA.getUsername());
        SessionContext.logoutUser(userB.getUsername());

        userService.delete(userA);
        userService.delete(userB);
    }

    @SneakyThrows
    @Test
    void battleShouldWork() {
        String userABattlesRequest = postNoContentRequest(BATTLES_ENDPOINT, userA.getUsername());
        String userBBattlesRequest = postNoContentRequest(BATTLES_ENDPOINT, userB.getUsername());

        Request request1 = HttpRequest.valueOf(new ByteArrayInputStream(userABattlesRequest.getBytes(StandardCharsets.UTF_8)));
        Request request2 = HttpRequest.valueOf(new ByteArrayInputStream(userBBattlesRequest.getBytes(StandardCharsets.UTF_8)));

        CompletionService<Response> service = new ExecutorCompletionService<>(WORKER_THREAD_POOL);
        Future<Response> userAResponse = service.submit(() -> battlesController.handleRequest(request1));
        Future<Response> userBResponse = service.submit(() -> battlesController.handleRequest(request2));

        assertEquals(NO_CONTENT.getCode(), userAResponse.get().getStatusCode());
        assertEquals(NO_CONTENT.getCode(), userBResponse.get().getStatusCode());

        userA = userService.findById(userA.getId());
        userB = userService.findById(userB.getId());

        assertTrue(userA.getCards().size() == 8 && userB.getDeck().isEmpty()
                || userB.getCards().size() == 8 && userA.getDeck().isEmpty() );
    }
}