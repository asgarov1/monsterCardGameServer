package at.fhtw.bif3.service;

import at.fhtw.bif3.domain.TradingDeal;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.domain.card.Card;
import at.fhtw.bif3.domain.card.GoblinCard;
import org.junit.jupiter.api.*;

import static at.fhtw.bif3.domain.card.ElementType.WATER;
import static at.fhtw.bif3.util.NumberUtil.randomInt;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TradingDealServiceTest {

    TradingDealService service = new TradingDealService();
    static UserService userService = new UserService();
    static CardService cardService = new CardService();

    static User user = new User("Tom", "pass");
    static Card card = new GoblinCard("tradingDeal_testId1", "test_name", randomInt(0, 100), WATER);
    TradingDeal tradingDeal = new TradingDeal("tradingDeal_testId2", card, card.getCardType(), 50, user);

    @BeforeAll
    static void setUp(){
        userService.create(user);
        cardService.create(card);
    }

    @AfterAll
    static void clean() {
        userService.delete(user);
        cardService.delete(card);
    }

    @Test
    void createShouldWork() {
        int countEntities = service.countEntities();

        service.create(tradingDeal);
        assertEquals(countEntities + 1, service.countEntities());
    }

    @Test
    void updateShouldWork() {
        service.create(tradingDeal);

        int minDamage = 25;
        tradingDeal.setMinimumDamage(minDamage);
        service.update(tradingDeal);

        assertEquals(minDamage, tradingDeal.getMinimumDamage());
    }

}