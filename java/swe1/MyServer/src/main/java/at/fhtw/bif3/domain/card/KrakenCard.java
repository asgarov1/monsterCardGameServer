package at.fhtw.bif3.domain.card;

public class KrakenCard extends Card {

    public KrakenCard() {
    }

    public KrakenCard(String id, String name, double damage, ElementType elementType) {
        super(id, name, damage, elementType);
    }

    @Override
    protected void setCardClass() {
        this.cardClass = CardClass.KRAKEN;
    }
}
