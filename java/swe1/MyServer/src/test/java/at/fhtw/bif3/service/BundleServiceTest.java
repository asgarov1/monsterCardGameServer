package at.fhtw.bif3.service;

import at.fhtw.bif3.domain.Bundle;
import at.fhtw.bif3.domain.Card;
import at.fhtw.bif3.domain.CardType;
import at.fhtw.bif3.domain.ElementType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BundleServiceTest {

    private final BundleService bundleService = new BundleService();
    private Bundle bundle;
    static List<Card> cards = List.of(
                new Card("test_id1", "test_name1", new Random().nextInt(), ElementType.FIRE, CardType.MONSTER),
                new Card("test_id2", "test_name2", new Random().nextInt(), ElementType.WATER, CardType.SPELL),
                new Card("test_id3", "test_name3", new Random().nextInt(), ElementType.NORMAL, CardType.MONSTER));

    @BeforeAll
    static void prepareCards(){
        var cardService = new CardService();
        cards.forEach(cardService::save);
    }

    @AfterAll
    static void cleanUpCards(){
        var cardService = new CardService();
        cards.stream().map(Card::getId).forEach(cardService::delete);
    }

    @AfterEach
    private void cleanUp(){
        if(bundle.getId() != null) {
            bundleService.delete(bundle.getId());
        }
    }

    @Test
    void create() {
        int entitiesBefore = bundleService.countEntities();

        bundle = new Bundle("test_bundle_id", cards);
        bundleService.save(bundle);

        assertEquals(entitiesBefore + 1, bundleService.countEntities());

        var readBundle = bundleService.findById(bundle.getId());
        assertNotNull(readBundle);
        assertEquals(bundle, readBundle);
    }

    @Test
    void update() {
        bundle = new Bundle("test_bundle_id", cards);
        bundleService.save(bundle);

        bundle.setCards(new ArrayList<>());
        bundleService.update(bundle);

        assertTrue(bundleService.findById(bundle.getId()).getCards().isEmpty());
    }

    @Test
    void delete() {
        bundle = new Bundle("test_bundle_id", cards);
        bundleService.save(bundle);

        int entitiesBefore = bundleService.countEntities();

        bundleService.delete(bundle.getId());

        assertEquals(entitiesBefore-1, bundleService.countEntities());
    }

    @Test
    void read() {
        bundle = new Bundle("test_bundle_id", cards);
        bundleService.save(bundle);

        var readCard = bundleService.findById(bundle.getId());
        assertNotNull(readCard);
        assertEquals(bundle, readCard);
    }

    @Test
    void countEntities() {
        int entitiesBefore = bundleService.countEntities();

        bundle = new Bundle("test_bundle_id", cards);
        bundleService.save(bundle);

        assertEquals(entitiesBefore + 1, bundleService.countEntities());
    }
}