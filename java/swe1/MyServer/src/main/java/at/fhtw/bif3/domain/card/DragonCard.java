package at.fhtw.bif3.domain.card;

import static at.fhtw.bif3.domain.card.ElementType.FIRE;

public class DragonCard extends Card {
    @Override
    public double calculateDamageAgainst(Card card) {
        if(card.getElementType().equals(FIRE) && card instanceof ElfCard){
            return 0; //because FireElves evade dragon attacks
        }
        return getDamage();
    }
}
