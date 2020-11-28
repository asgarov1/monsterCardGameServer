package at.fhtw.bif3.service;

import at.fhtw.bif3.domain.card.*;
import at.fhtw.bif3.domain.User;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

class BattleServiceTest {

    @Test
    void performBattle() {
        User userA = new User("userA", "pass");
        User userB = new User("userB", "pass");

        userA.setCards(Stream.of(
                new ElfCard("test_id1", "Firemonster", new Random().nextInt(100), ElementType.FIRE),
                new SpellCard("test_id2", "Water Spell", new Random().nextInt(100), ElementType.WATER),
                new DragonCard("test_id3", "Earth Elementel", new Random().nextInt(100), ElementType.NORMAL),
                new WizardCard("test_id4", "Werewolf", new Random().nextInt(100),  ElementType.NORMAL)).collect(toList())
        );

        userB.setCards(Stream.of(
                new DragonCard("test_id1", "Fire Dragon", new Random().nextInt(100), ElementType.FIRE),
                new SpellCard("test_id2", "Ice Spell", new Random().nextInt(100), ElementType.WATER),
                new GoblinCard("test_id3", "Tigerman", new Random().nextInt(100), ElementType.NORMAL),
                new OrkCard("test_id4", "Cursed Tree", new Random().nextInt(100), ElementType.NORMAL)).collect(toList())
        );

        new BattleService().performBattle(userA, userB);
    }
}