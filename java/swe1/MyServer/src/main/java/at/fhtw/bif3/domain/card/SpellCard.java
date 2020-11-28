package at.fhtw.bif3.domain.card;

import static at.fhtw.bif3.domain.card.CardType.SPELL;
import static at.fhtw.bif3.domain.card.ElementType.FIRE;
import static at.fhtw.bif3.domain.card.ElementType.WATER;
import static at.fhtw.bif3.util.PropertiesReader.getProperties;
import static java.lang.Integer.parseInt;

public class SpellCard extends Card {

    public static final int DAMAGE_AGAINST_KRAKEN = parseInt(getProperties().getProperty("damage.spell-vs-kraken"));;
    public static final double DAMAGE_AGAINST_KNIGHT = Double.MAX_VALUE;

    @Override
    public double calculateDamageAgainst(Card opponent) {
        if(opponent instanceof KrakenCard){
            return DAMAGE_AGAINST_KRAKEN; //Kraken immune against spells
        } else if (getElementType().equals(WATER) && opponent instanceof KnightCard){
            return DAMAGE_AGAINST_KNIGHT; //Knights automatically drown to water spell
        } else if (opponent instanceof SpellCard){
            return getPureSpellBattleDamage(opponent);
        }
        return getDamage();
    }

    private double getPureSpellBattleDamage(Card opponent) {
        if (getElementType().equals(WATER) && opponent.getElementType().equals(FIRE)) {
            return getDamage() * 2;
        } else if (getElementType().equals(FIRE) && opponent.getElementType().equals(WATER)) {
            return getDamage() / 2;
        }
        return getDamage();
    }

    @Override
    protected void setCardType() {
        this.cardType = SPELL;
    }

    public SpellCard() {
    }

    public SpellCard(String id, String name, int damage, ElementType elementType) {
        super(id, name, damage, elementType);
    }
}
