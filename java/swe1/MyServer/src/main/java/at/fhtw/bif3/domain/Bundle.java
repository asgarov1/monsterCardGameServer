package at.fhtw.bif3.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bundle {
    private String id;
    private List<Card> cards = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bundle bundle = (Bundle) o;
        return Objects.equals(id, bundle.id) &&
                Objects.equals(cards.size(), bundle.cards.size());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cards);
    }
}
