package at.fhtw.bif3.domain.card;

public class OrkCard extends Card {
    @Override
    public double calculateDamageAgainst(Card card) {
        if (card instanceof WizardCard) {
            return 0; //Wizards control Orks so these can't damage them
        }
        return super.calculateDamageAgainst(card);
    }
}
