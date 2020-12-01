package at.fhtw.bif3.domain.card;

import java.util.Arrays;

public enum CardClass {
    SPELL,
    GOBLIN,
    DRAGON,
    WIZARD,
    ORK,
    KNIGHT,
    KRAKEN,
    ELF;

    public static CardClass assignByName(String name) {
        return Arrays.stream(values())
                .filter(value -> name.toLowerCase().contains(value.name().toLowerCase()))
                .findFirst()
                .orElseThrow();
    }

    public Card instantiateByType() {
        if (this.equals(SPELL)) {
            return new SpellCard();
        } else if (this.equals(GOBLIN)) {
            return new GoblinCard();
        } else if (this.equals(DRAGON)) {
            return new DragonCard();
        } else if (this.equals(WIZARD)) {
            return new WizardCard();
        } else if (this.equals(ORK)) {
            return new OrkCard();
        } else if (this.equals(KNIGHT)) {
            return new KnightCard();
        } else if (this.equals(KRAKEN)) {
            return new KrakenCard();
        }
        return new ElfCard();
    }

    public boolean isSpellClass() {
        return this.equals(SPELL);
    }
}
