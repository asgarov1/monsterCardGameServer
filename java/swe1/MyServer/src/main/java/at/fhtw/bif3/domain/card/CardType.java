package at.fhtw.bif3.domain.card;

import java.util.Arrays;

public enum CardType {
    MONSTER,
    SPELL;

    public static CardType assignByName(String name) {
        return Arrays.stream(values())
                .filter(value -> name.toLowerCase().contains(value.name().toLowerCase()))
                .findFirst()
                .orElseThrow();

    }
}
