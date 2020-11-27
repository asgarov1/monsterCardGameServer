package at.fhtw.bif3.service;

import at.fhtw.bif3.domain.Card;
import at.fhtw.bif3.domain.CardType;
import at.fhtw.bif3.domain.ElementType;
import at.fhtw.bif3.domain.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;

class BattleServiceTest {

    @Test
    void performBattle() {
        User userA = new User("userA", "pass");
        User userB = new User("userB", "pass");

        userA.setCards(Stream.of(
                new Card("test_id1", "Firemonster", new Random().nextInt(100), new Random().nextInt(100), ElementType.FIRE, CardType.MONSTER),
                new Card("test_id2", "Water Spell", new Random().nextInt(100), new Random().nextInt(100), ElementType.WATER, CardType.SPELL),
                new Card("test_id3", "Earth Elementel", new Random().nextInt(100), new Random().nextInt(100), ElementType.NORMAL, CardType.MONSTER),
                new Card("test_id4", "Werewolf", new Random().nextInt(100), new Random().nextInt(100), ElementType.NORMAL, CardType.MONSTER)).collect(toList())
        );

        userB.setCards(Stream.of(
                new Card("test_id1", "Fire Dragon", new Random().nextInt(100), new Random().nextInt(100), ElementType.FIRE, CardType.MONSTER),
                new Card("test_id2", "Ice Spell", new Random().nextInt(100), new Random().nextInt(100), ElementType.WATER, CardType.SPELL),
                new Card("test_id3", "Tigerman", new Random().nextInt(100), new Random().nextInt(100), ElementType.NORMAL, CardType.MONSTER),
                new Card("test_id4", "Cursed Tree", new Random().nextInt(100), new Random().nextInt(100), ElementType.NORMAL, CardType.MONSTER)).collect(toList())
        );

        new BattleService().performBattle(userA, userB);
    }
}