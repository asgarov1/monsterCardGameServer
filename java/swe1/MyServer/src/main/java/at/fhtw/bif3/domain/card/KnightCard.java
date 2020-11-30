package at.fhtw.bif3.domain.card;

public class KnightCard extends Card {

    public KnightCard() {
    }

    public KnightCard(String id, String name, double damage, ElementType elementType) {
        super(id, name, damage, elementType);
    }

    @Override
    protected void setCardClass() {
        this.cardClass = CardClass.KNIGHT;
    }
}
