package at.fhtw.bif3.domain;

import java.util.Arrays;

public enum ElementType {
    WATER,
    FIRE,
    NORMAL;

    public static ElementType assignByName(String name) {
        return Arrays.stream(values()).filter(value -> value.name().equals(name)).findFirst().orElseThrow();
    }
}
