package at.fhtw.bif3.domain.card;

public class WizardCard extends Card {

    public WizardCard() {
    }

    public WizardCard(String id, String name, double damage, ElementType elementType) {
        super(id, name, damage, elementType);
    }

    @Override
    protected void setCardClass() {
        this.cardClass = CardClass.WIZARD;
    }
}
