package at.fhtw.bif3.domain.card;

import static at.fhtw.bif3.domain.card.ElementType.FIRE;
import static at.fhtw.bif3.domain.card.ElementType.WATER;

public class SpellCard extends Card {
    @Override
    public double calculateDamageAgainst(Card opponent) {
        if(opponent instanceof KrakenCard){
            return 0; //Kraken immune against spells
        } else if (getElementType().equals(WATER) && opponent instanceof KnightCard){
            return Double.MAX_VALUE; //Knights automatically drown to water spell
        } else if (opponent instanceof SpellCard){
            //PURE SPELL FIGHT LOGIC
            if (getElementType().equals(WATER) && opponent.getElementType().equals(FIRE)) {
                return getDamage() * 2;
            } else if (getElementType().equals(FIRE) && opponent.getElementType().equals(WATER)) {
                return getDamage() / 2;
            }
        }
        return getDamage();
    }
}
