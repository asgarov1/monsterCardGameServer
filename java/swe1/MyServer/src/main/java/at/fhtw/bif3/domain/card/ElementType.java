package at.fhtw.bif3.domain.card;

import java.util.Arrays;

public enum ElementType {
    WATER,
    FIRE,
    NORMAL;

    public static ElementType assignByName(String name) {
        return Arrays.stream(values()).filter(value -> name.toLowerCase().contains(value.name().toLowerCase())).findFirst().orElse(NORMAL);
    }
}
