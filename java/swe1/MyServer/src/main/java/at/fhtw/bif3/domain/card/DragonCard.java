package at.fhtw.bif3.domain.card;

import static at.fhtw.bif3.domain.card.ElementType.FIRE;
import static at.fhtw.bif3.util.PropertiesReader.getProperties;
import static java.lang.Integer.parseInt;

public class DragonCard extends Card {

    public static final int DAMAGE_AGAINST_FILE_ELF = parseInt(getProperties().getProperty("damage.dragon-vs-fire-elf"));

    public DragonCard() {
    }

    public DragonCard(String id, String name, int damage, ElementType elementType) {
        super(id, name, damage, elementType);
    }

    @Override
    public double calculateDamageAgainst(Card card) {
        if(card.getElementType().equals(FIRE) && card instanceof ElfCard){
            return DAMAGE_AGAINST_FILE_ELF; //because FireElves evade dragon attacks
        }
        return getDamage();
    }

    @Override
    protected void setCardType() {
        this.cardType = CardType.DRAGON;
    }
}
