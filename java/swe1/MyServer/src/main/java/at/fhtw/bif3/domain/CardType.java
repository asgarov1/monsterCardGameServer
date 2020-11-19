package at.fhtw.bif3.domain;

import java.util.Arrays;

public enum CardType {
    SPELL,
    MONSTER;

    public static CardType findByName(String name){
        return Arrays.stream(values())
                .filter(value -> value.name().equals(name))
                .findFirst()
                .orElseThrow();
    }

    //TODO check for additional logic based on monster types
//    Goblins are too afraid of Dragons to attack.
//    Wizzard can control Orks so they are not able to damage them.
//    The armor of Knights is so heavy that WaterSpells make them drown instantly.
//    The Kraken is immune against spells.
//    The FireElves know Dragons since they were little and can evade their attacks.
}
