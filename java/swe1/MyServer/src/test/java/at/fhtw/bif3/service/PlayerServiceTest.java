package at.fhtw.bif3.service;

import at.fhtw.bif3.domain.card.*;
import at.fhtw.bif3.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlayerServiceTest {

    private final UserService playerService = new UserService();
    private final CardService cardService = new CardService();
    User player;

    @AfterEach
    void cleanUp() {
        if (player.getId() != null) {
            player.getCards().forEach(card -> cardService.delete(card.getId()));
            playerService.delete(player.getId());
        }
    }

    @Test
    void create() {
        int entitiesBefore = playerService.countEntities();

        player = new User("test_id", "test_username", "test_password");
        playerService.create(player);

        assertEquals(entitiesBefore + 1, playerService.countEntities());

        var readPlayer = playerService.findById(player.getId());
        assertNotNull(readPlayer);
        assertEquals(player, readPlayer);
    }

    @Test
    void update() {
        player = new User("test_id", "test_username", "test_password");
        playerService.create(player);

        int numberOfCoins = 25;
        player.setNumberOfCoins(numberOfCoins);
        playerService.update(player);

        assertEquals(numberOfCoins, playerService.findById(player.getId()).getNumberOfCoins());
    }

    @Test
    void delete() {
        player = new User("test_id", "test_username", "test_password");
        playerService.create(player);

        int entitiesBefore = playerService.countEntities();

        playerService.delete(player.getId());

        assertEquals(entitiesBefore - 1, playerService.countEntities());
    }

    @Test
    void read() {
        var cards = List.of(
                new ElfCard("test_id1", "test_name1", new Random().nextDouble(), ElementType.FIRE),
                new SpellCard("test_id2", "test_name2", new Random().nextDouble(), ElementType.WATER));

        player = new User("test_id", "test_username", "test_password");
        cards.forEach(cardService::create);

        player.setCards(cards);
        playerService.create(player);

        var readPlayer = playerService.findById(player.getId());
        assertNotNull(readPlayer);
        assertEquals(player, readPlayer);
    }
}