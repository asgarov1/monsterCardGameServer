package at.fhtw.bif3.domain.card;

public class WizardCard extends Card {

    public WizardCard() {
    }

    public WizardCard(String id, String name, int damage, ElementType elementType) {
        super(id, name, damage, elementType);
    }

    @Override
    protected void setCardType() {
        this.cardType = CardType.WIZARD;
    }
}
