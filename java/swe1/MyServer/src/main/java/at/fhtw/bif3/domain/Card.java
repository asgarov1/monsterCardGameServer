package at.fhtw.bif3.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private String id;
    private String name;
    private int damage;
    private ElementType elementType;
    private CardType cardType;
}
