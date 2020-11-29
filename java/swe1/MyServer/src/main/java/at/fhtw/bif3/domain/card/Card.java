package at.fhtw.bif3.domain.card;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Card {
    @SerializedName("Id")
    String id;

    @SerializedName("Name")
    String name;

    @SerializedName("Damage")
    double damage;

    ElementType elementType;

    @Setter(AccessLevel.NONE)
    CardType cardType;

    { setCardType(); }

    public Card(String id, String name, double damage, ElementType elementType) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Double.compare(card.damage, damage) == 0 &&
                Objects.equals(id, card.id) &&
                Objects.equals(name, card.name) &&
                elementType == card.elementType &&
                cardType == card.cardType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, damage, elementType, cardType);
    }
}
