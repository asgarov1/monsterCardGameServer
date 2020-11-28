package at.fhtw.bif3.domain.card;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public abstract class Card {
    @SerializedName("Id")
    private String id;

    @SerializedName("Name")
    private String name;

    @SerializedName("Damage")
    private double damage;

    @SerializedName("Weakness")
    private double weakness;
    private ElementType elementType;
    private CardType cardType;

    public Card(String id, String name, int damage, String elementType, String cardType) {
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.elementType = ElementType.assignByName(elementType);
        this.cardType = CardType.assignByName(cardType);
    }

    public Card(String id, String name, int damage) {
        this.id = id;
        this.name = name;
        this.damage = damage;
    }

    public double calculateDamageAgainst(Card card) {
        return damage;
    }
}
