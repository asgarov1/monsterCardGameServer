package at.fhtw.bif3.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private String id;
    private String name;
    private int damage;
    private ElementType elementType;
    private CardType cardType;
}
