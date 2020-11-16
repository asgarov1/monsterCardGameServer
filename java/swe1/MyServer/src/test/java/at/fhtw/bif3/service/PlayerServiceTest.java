package at.fhtw.bif3.service;

import at.fhtw.bif3.domain.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerServiceTest {

    //TODO write tests for changes with cards

    private final PlayerService playerService = new PlayerService();
    Player player;

    @AfterEach
    private void cleanUp(){
        if(player.getId() != null) {
            playerService.delete(player.getId());
        }
    }

    @Test
    void create() {
        int entitiesBefore = playerService.countEntities();

        player = new Player("test_id", "test_username", "test_password");
         playerService.save(player);

        assertEquals(entitiesBefore + 1, playerService.countEntities());

        var readPlayer = playerService.findById(player.getId());
        assertNotNull(readPlayer);
        assertEquals(player, readPlayer);
    }

    @Test
    void update() {
        player = new Player("test_id", "test_username", "test_password");
        playerService.save(player);

        int numberOfCoins = 25;
        player.setNumberOfCoins(numberOfCoins);
        playerService.update(player);

        assertEquals(numberOfCoins, playerService.findById(player.getId()).getNumberOfCoins());
    }

    @Test
    void delete() {
        player = new Player("test_id", "test_username", "test_password");
        playerService.save(player);

        int entitiesBefore = playerService.countEntities();

        playerService.delete(player.getId());

        assertEquals(entitiesBefore-1, playerService.countEntities());
    }

    @Test
    void read() {
        player = new Player("test_id", "test_username", "test_password");
        playerService.save(player);

        var readPlayer = playerService.findById(player.getId());
        assertNotNull(readPlayer);
        assertEquals(player, readPlayer);
    }
}