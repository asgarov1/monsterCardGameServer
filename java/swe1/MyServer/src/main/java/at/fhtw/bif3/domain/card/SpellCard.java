package at.fhtw.bif3.domain.card;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import static at.fhtw.bif3.domain.card.CardClass.SPELL;
import static at.fhtw.bif3.domain.card.ElementType.FIRE;
import static at.fhtw.bif3.domain.card.ElementType.WATER;
import static at.fhtw.bif3.util.PropertiesReader.getProperties;
import static java.lang.Integer.parseInt;

public class SpellCard extends Card {


    public SpellCard() {
    }

    public SpellCard(String id, String name, double damage, ElementType elementType) {
        super(id, name, damage, elementType);
    }

    public static final int DAMAGE_AGAINST_KRAKEN = parseInt(getProperties().getProperty("damage.spell-vs-kraken"));;
    public static final double DAMAGE_AGAINST_KNIGHT = Double.MAX_VALUE;

    @Getter
    @Setter
    @SerializedName("Weakness")
    private double weakness; //TODO question: how does weakness work for spells?
//    curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" -d "[{\"Id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"Name\":\"WaterGoblin\", \"Damage\": 10.0}, {\"Id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"Name\":\"Dragon\", \"Damage\": 50.0}, {\"Id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"Name\":\"WaterSpell\", \"Weakness\": 45.0, \"Damage\": 20.0}, {\"Id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Name\":\"Ork\", \"Damage\": 45.0}, {\"Id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\", \"Name\":\"FireSpell\", \"Weakness\": 45.0, \"Damage\": 25.0}]"


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
    protected void setCardClass() {
        this.cardClass = SPELL;
    }

}
