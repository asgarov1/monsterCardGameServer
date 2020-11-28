package at.fhtw.bif3.domain.card;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public abstract class Card {
    @SerializedName("Id")
    String id;

    @SerializedName("Name")
    String name;

    @SerializedName("Damage")
    double damage;

    @SerializedName("Weakness")
    double weakness; //TODO question: what does weakness mean?
//    curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" -d "[{\"Id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"Name\":\"WaterGoblin\", \"Damage\": 10.0}, {\"Id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"Name\":\"Dragon\", \"Damage\": 50.0}, {\"Id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"Name\":\"WaterSpell\", \"Weakness\": 45.0, \"Damage\": 20.0}, {\"Id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Name\":\"Ork\", \"Damage\": 45.0}, {\"Id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\", \"Name\":\"FireSpell\", \"Weakness\": 45.0, \"Damage\": 25.0}]"
    ElementType elementType;

    @Setter(AccessLevel.NONE)
    CardType cardType;

    public Card(String id, String name, int damage, ElementType elementType) {
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.elementType = elementType;
    }

    public Card(String id, String name, int damage) {
        this.id = id;
        this.name = name;
        this.damage = damage;
    }

    public double calculateDamageAgainst(Card card) {
        return damage;
    }

    protected abstract void setCardType();
}

