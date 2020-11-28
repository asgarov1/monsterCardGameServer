package at.fhtw.bif3.domain.card;

public class GoblinCard extends Card {
    @Override
    public double calculateDamageAgainst(Card card) {
        if (card instanceof DragonCard) {
            return 0; //Goblins are too afraid of Dragons to attack
        }
        return super.calculateDamageAgainst(card);
    }
}
