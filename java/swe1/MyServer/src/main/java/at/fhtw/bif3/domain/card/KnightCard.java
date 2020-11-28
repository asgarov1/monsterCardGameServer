package at.fhtw.bif3.domain.card;

public class KnightCard extends Card {

    public KnightCard() {
    }

    public KnightCard(String id, String name, int damage, ElementType elementType) {
        super(id, name, damage, elementType);
    }

    @Override
    protected void setCardType() {
        this.cardType = CardType.KNIGHT;
    }
}
