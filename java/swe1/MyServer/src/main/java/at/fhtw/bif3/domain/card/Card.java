package at.fhtw.bif3.domain.card;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.Objects;

import static at.fhtw.bif3.domain.card.CardType.MONSTER;
import static at.fhtw.bif3.domain.card.CardType.SPELL;

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
    CardClass cardClass;

    CardType type;

    {
        setCardClass();
        if (cardClass.isSpellClass()) {
            type = SPELL;
        } else {
            type = MONSTER;
        }
    }

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

    protected abstract void setCardClass();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Double.compare(card.damage, damage) == 0 &&
                Objects.equals(id, card.id) &&
                Objects.equals(name, card.name) &&
                elementType == card.elementType &&
                cardClass == card.cardClass;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, damage, elementType, cardClass);
    }

    @Override
    public String toString() {
        return "Card{" +
                 name + '\'' +
                ", " + damage +
                ", " + elementType +
                '}';
    }
}
