package at.fhtw.bif3.domain.card;

public class ElfCard extends Card {

    public ElfCard() {
    }

    public ElfCard(String id, String name, double damage, ElementType elementType) {
        super(id, name, damage, elementType);
    }

    @Override
    protected void setCardType() {
        this.cardType = CardType.ELF;
    }
}
