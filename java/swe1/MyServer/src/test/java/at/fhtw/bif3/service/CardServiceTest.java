package at.fhtw.bif3.service;

import at.fhtw.bif3.domain.card.Card;
import at.fhtw.bif3.domain.card.CardType;
import at.fhtw.bif3.domain.card.ElementType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CardServiceTest {

    private final CardService cardService = new CardService();
    private Card card;

    @AfterEach
    private void cleanUp() {
        if (card.getId() != null) {
            cardService.delete(card.getId());
        }
    }

    @Test
    void create() {
        int entitiesBefore = cardService.countEntities();

        card = new Card("test_id", "test_name", new Random().nextDouble(), new Random().nextDouble(), ElementType.FIRE, CardType.KRAKEN);
        cardService.create(card);

        assertEquals(entitiesBefore + 1, cardService.countEntities());

        var readCard = cardService.findById(card.getId());
        assertNotNull(readCard);
        assertEquals(card, readCard);
    }

    @Test
    void update() {
        card = new Card("test_id", "test_name", new Random().nextDouble(), new Random().nextDouble(), ElementType.FIRE, CardType.GOBLIN);
        cardService.create(card);

        int newDamage = 100;
        card.setDamage(newDamage);
        cardService.update(card);

        assertEquals(newDamage, cardService.findById(card.getId()).getDamage());
    }

    @Test
    void delete() {
        card = new Card("test_id", "test_name", new Random().nextDouble(), new Random().nextDouble(), ElementType.FIRE, CardType.DRAGON);
        cardService.create(card);

        int entitiesBefore = cardService.countEntities();

        cardService.delete(card.getId());

        assertEquals(entitiesBefore - 1, cardService.countEntities());
    }

    @Test
    void read() {
        card = new Card("test_id", "test_name", new Random().nextDouble(), new Random().nextDouble(), ElementType.FIRE, CardType.KRAKEN);
        cardService.create(card);

        var readCard = cardService.findById(card.getId());
        assertNotNull(readCard);
        assertEquals(card, readCard);
    }

    @Test
    void countEntities() {
        int entitiesBefore = cardService.countEntities();

        card = new Card("test_id", "test_name", new Random().nextDouble(), new Random().nextDouble(), ElementType.FIRE, CardType.WIZARD);
        cardService.create(card);

        assertEquals(entitiesBefore + 1, cardService.countEntities());
    }
}