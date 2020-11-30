package at.fhtw.bif3.domain.card;

import lombok.Builder;

import static at.fhtw.bif3.domain.card.CardClass.GOBLIN;
import static at.fhtw.bif3.util.PropertiesReader.getProperties;
import static java.lang.Integer.parseInt;

@Builder
public class GoblinCard extends Card {

    public static final int DAMAGE_AGAINST_DRAGON = parseInt(getProperties().getProperty("damage.goblin-vs-dragon"));

    public GoblinCard() {
    }

    public GoblinCard(String id, String name, double damage, ElementType elementType) {
        super(id, name, damage, elementType);
    }

    @Override
    public double calculateDamageAgainst(Card card) {
        if (card instanceof DragonCard) {
            return DAMAGE_AGAINST_DRAGON; //Goblins are too afraid of Dragons to attack
        }
        return super.calculateDamageAgainst(card);
    }

    @Override
    protected void setCardClass() {
        this.cardClass = GOBLIN;
    }
}
