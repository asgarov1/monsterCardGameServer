package at.fhtw.bif3.service;

import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.domain.card.ElementType;
import at.fhtw.bif3.domain.card.ElfCard;
import at.fhtw.bif3.domain.card.SpellCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserServiceTest {

    private final UserService playerService = new UserService();
    private final CardService cardService = new CardService();
    private final User player = new User("UserServiceTest_user_id", "test_username", "test_password");

    @AfterEach
    void cleanUp() {
        playerService.delete(player);
    }

    @Test
    void create() {
        int entitiesBefore = playerService.countEntities();

        playerService.create(player);

        assertEquals(entitiesBefore + 1, playerService.countEntities());

        var readPlayer = playerService.findById(player.getId());
        assertNotNull(readPlayer);
        assertEquals(player, readPlayer);
    }

    @Test
    void update() {
        playerService.create(player);

        int numberOfCoins = 25;
        player.setNumberOfCoins(numberOfCoins);
        playerService.update(player);

        assertEquals(numberOfCoins, playerService.findById(player.getId()).getNumberOfCoins());
    }

    @Test
    void delete() {
        playerService.create(player);

        int entitiesBefore = playerService.countEntities();

        playerService.delete(player.getId());

        assertEquals(entitiesBefore - 1, playerService.countEntities());
    }

    @Test
    void read() {
        var cards = List.of(
                new ElfCard("UserServiceTest_card_id1", "test_name1", new Random().nextDouble(), ElementType.FIRE),
                new SpellCard("UserServiceTest_card_id2", "test_name2", new Random().nextDouble(), ElementType.WATER));

        cards.forEach(cardService::create);

        player.setCards(cards);
        playerService.create(player);

        var readPlayer = playerService.findById(player.getId());
        assertNotNull(readPlayer);
        assertEquals(player, readPlayer);
    }
}